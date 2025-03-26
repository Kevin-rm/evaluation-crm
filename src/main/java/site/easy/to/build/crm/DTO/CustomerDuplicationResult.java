package site.easy.to.build.crm.DTO;

import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;

import java.time.LocalDate;
import java.util.List;

public record CustomerDuplicationResult(
    Integer customerId,
    String name,
    String email,
    String country,
    List<LeadDuplication> leads,
    List<TicketDuplication> tickets
) {
    public record LeadDuplication(
        Integer leadId,
        String name,
        String status,
        String phone,
        ExpenseDuplication expenseDuplication
    ) {

        public static LeadDuplication createFromLead(Lead lead) {
            return new LeadDuplication(lead.getLeadId(), lead.getName(), lead.getStatus(), lead.getPhone(), ExpenseDuplication.createFromExpense(lead.getExpense()));
        }
    }

    public record TicketDuplication(
        Integer tickedId,
        String subject,
        String status,
        String priority,
        ExpenseDuplication expenseDuplication
    ) {

        public static TicketDuplication createFromTicket(Ticket ticket) {
            return new TicketDuplication(ticket.getTicketId(), ticket.getSubject(), ticket.getStatus(), ticket.getPriority(), ExpenseDuplication.createFromExpense(ticket.getExpense()));
        }
    }

    public record ExpenseDuplication(
        Integer expenseId,
        Double amount,
        LocalDate dateExpense
    ) {

        public static ExpenseDuplication createFromExpense(Expense expense) {
            if (expense == null) {
                return null;
            }

            return new ExpenseDuplication(expense.getExpenseId(), expense.getAmount(), expense.getDateExpense());
        }

        public Expense toExpense() {
            Expense expense = new Expense();
            expense.setAmount(amount);
            expense.setDateExpense(dateExpense);

            return expense;
        }
    }
}
