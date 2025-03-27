package site.easy.to.build.crm.service.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.DTO.CustomerDuplicationResult;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final LeadService leadService;
    private final TicketService ticketService;

    @Override
    public Customer findByCustomerId(int customerId) {
        return customerRepository.findByCustomerId(customerId);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public List<Customer> findByUserId(int userId) {
        return customerRepository.findByUserId(userId);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public List<Customer> getRecentCustomers(int userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return customerRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    public long countByUserId(int userId) {
        return customerRepository.countByUserId(userId);
    }

    @Override
    public CustomerDuplicationResult duplicate(Customer customer) {
        /*
         * - Récupérer tous les leads (associé à une dépense) de ce customer
         * - Récupérer tous les tickets (associé à une dépense) de ce customer
         */
        Integer customerId = customer.getCustomerId();
        List<CustomerDuplicationResult.LeadDuplication> leads = leadService.getCustomerLeads(customerId).stream().map(CustomerDuplicationResult.LeadDuplication::createFromLead).toList();
        List<CustomerDuplicationResult.TicketDuplication> tickets = ticketService.findCustomerTickets(customerId).stream().map(CustomerDuplicationResult.TicketDuplication::createFromTicket).toList();

        return new CustomerDuplicationResult(customerId, customer.getName(), String.format("copy_%s", customer.getEmail()), customer.getCountry(), leads, tickets);
    }

    @Transactional
    @Override
    public void save(CustomerDuplicationResult customerDuplicationResult) {
        Customer customer = new Customer();
        customer.setName(customerDuplicationResult.name());
        customer.setEmail(customerDuplicationResult.email());
        customer.setCountry(customerDuplicationResult.country());

        customer = customerRepository.save(customer);

        for (CustomerDuplicationResult.LeadDuplication ld : customerDuplicationResult.leads()) {
            Lead lead = new Lead();
            lead.setName(ld.name());
            lead.setStatus(ld.status());
            lead.setPhone(ld.phone());
            lead.setCustomer(customer);

            CustomerDuplicationResult.ExpenseDuplication expenseDuplication = ld.expenseDuplication();
            if (expenseDuplication != null) lead.setExpense(expenseDuplication.toExpense());

            leadService.save(lead);
        }

        for (CustomerDuplicationResult.TicketDuplication td : customerDuplicationResult.tickets()) {
            Ticket ticket = new Ticket();
            ticket.setSubject(td.subject());
            ticket.setStatus(td.status());
            ticket.setPriority(td.priority());
            ticket.setCustomer(customer);

            CustomerDuplicationResult.ExpenseDuplication expenseDuplication = td.expenseDuplication();
            if (expenseDuplication != null) ticket.setExpense(expenseDuplication.toExpense());

            ticketService.save(ticket);
        }

        log.info("Customer imported successfully");
    }
}
