
package db61b;

import java.util.HashMap;
import java.util.ArrayList;


/** A collection of Tables, indexed by name.
 *  @author Daniel Kim*/
class Database {
    /** An empty database. */


    public Database() {
        _database = new HashMap<String, Table>();
    }

    /** Return the Table whose name is NAME stored in this database, or null
     *  if there is no such table. */
    public Table get(String name) {
        return _database.get(name);
    }

    /** Set or replace the table named NAME in THIS to TABLE.  TABLE and
     *  NAME must not be null, and NAME must be a valid name for a table. */
    public void put(String name, Table table) {
        if (name == null || table == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (_database.get(name) != null) {
            _database.remove(name);
        }
        _database.put(name, table);
    }

    /** Hashmap of names and tables.
     */
    private HashMap<String, Table> _database;
    ArrayList<HashMap> map;
}
