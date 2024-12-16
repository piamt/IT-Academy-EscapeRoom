package DAO.interfaces;

import exception.AddTicketFailedException;
import exception.CallFailedException;
import model.Ticket;

public interface TicketDAO extends DAO<Ticket> {
    void addTicket(Ticket ticket) throws AddTicketFailedException;
}
