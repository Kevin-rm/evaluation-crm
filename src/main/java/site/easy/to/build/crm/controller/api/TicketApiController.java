package site.easy.to.build.crm.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.response.ApiResponse;
import site.easy.to.build.crm.service.ExpenseService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/tickets")
public class TicketApiController {
    private final TicketService ticketService;
    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Ticket>>> getAll() {
        return ApiResponse.success(ticketService.findAll()).toResponseEntity();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
        Ticket ticket = ticketService.findByTicketId(id);
        if (ticket == null) return ApiResponse.notFound("Ticket not found").toResponseEntity();

        ticketService.delete(ticket);
        expenseService.delete(ticket.getExpense());

        return ApiResponse.success(String.format("Ticket with id %d deleted successfully", id))
            .toResponseEntity();
    }
}
