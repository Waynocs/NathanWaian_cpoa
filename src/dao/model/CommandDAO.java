package dao.model;

import model.Command;
import dao.DAO;

/**
 * Interface DAO utilise un type T cette class hérite de DAO, et l'objet de type
 * T se défini ici comme Command
 */
public interface CommandDAO extends DAO<Command> {

}
