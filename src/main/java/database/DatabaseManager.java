package database;

import utils.Utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class DatabaseManager {
    public DatabaseConnection getDbConnection() {
        return dbConnection;
    }

    private DatabaseConnection dbConnection;

    public DatabaseManager () {
        Properties prop = null;
        try {
            prop = Utils.readProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jdbcDriver = prop.getProperty("jdbc.driver");
        String prefix = prop.getProperty("db.prefix");
        String username = prop.getProperty("db.username");
        String password = prop.getProperty("db.password");
        String hostname = prop.getProperty("db.hostname");
        int port = Integer.parseInt(prop.getProperty("db.port"));
        String databaseName = prop.getProperty("db.name");

        dbConnection = new DatabaseConnection(jdbcDriver, prefix, username, password, hostname, port, databaseName);
    }

    public void search(Map<String, String> map) {
        if (map.containsKey("show"))
            searchNumber(map);
        else
            searchString(map);
    }

    public void searchString(Map<String, String> map) {
        String tablename;
        String columnname;
        String objectname;

        Map<String, String> info = getTableColumn(map);
        tablename = info.get("table");
        columnname = info.get("column");
        System.out.println(info);


        try {
            List<String> queries;


            objectname = map.get("obj");
            //System.out.printf("tablename: %s, columnname: %s, objname: %s", tablename, columnname, objectname);
            String searchTerm = "%"+objectname+"%";
            if (searchTerm.contains(" "))
                searchTerm = searchTerm.replace(' ', '%');
            if (tablename != null) {
                if (columnname != null) {
                    queries = DatabaseUtils.createQueries_objectInTableInColumn(dbConnection.getConnection(), dbConnection.getDatabaseName(), searchTerm, tablename, columnname);
                } else {
                    queries = DatabaseUtils.createQueries_objectInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(),searchTerm, tablename);
                }
            } else {
                if (columnname != null) {
                    queries = DatabaseUtils.createQueries_objectInColumn(dbConnection.getConnection(), dbConnection.getDatabaseName(), searchTerm, columnname);
                } else {
                    for (String table: DatabaseUtils.getTables(dbConnection.getConnection(), dbConnection.getDatabaseName())) {
                        queries = DatabaseUtils.createQueries_objectInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), searchTerm, table);
                        processQueries(queries);
                    }
                    return;
                }
            }

            processQueries(queries);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void searchNumber(Map<String, String> map) {
        // table, col, obj1, obj2, show
        String tablename;
        String columnname;
        String showname;
        String objectname;

        Map<String, String> info = getTableColumn(map);
        tablename = info.get("table");
        columnname = info.get("column");
        showname = info.get("show");
        System.out.println(info);

        String object1, object2;
        if (map.containsKey("obj1") && map.containsKey("obj2")) {
            object1 = map.get("obj1");
            object2 = map.get("obj2");

            try{
                List<String> queries;

                if (showname == null) {
                    if (tablename != null) {
                        if (columnname != null) {
                            queries = DatabaseUtils.createQueries_numberObjectBetweenInColumnInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), object1, object2, columnname, tablename);
                        } else {
                            queries = DatabaseUtils.createQueries_numberObjectBetweenInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), object1, object2, tablename);
                        }
                    } else {
                        if (columnname != null) {
                            queries = DatabaseUtils.createQueries_numberObjectBetweenInColumn(dbConnection.getConnection(), dbConnection.getDatabaseName(), object1, object2, columnname);
                        } else {
                            for (String table: DatabaseUtils.getTables(dbConnection.getConnection(), dbConnection.getDatabaseName())) {
                                queries = DatabaseUtils.createQueries_numberObjectBetweenInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), object1, object2, table);
                                processQueries(queries);
                            }
                            return;
                        }
                    }
                } else {
                    if (tablename != null) {
                        if (columnname != null) {
                            queries = DatabaseUtils.createQueries_numberObjectBetweenShowColumnInColumnInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), object1, object2, showname, columnname, tablename);
                        } else {
                            queries = DatabaseUtils.createQueries_numberObjectBetweenShowColumnInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), object1, object2, showname,tablename);
                        }
                    } else {
                        if (columnname != null) {
                            queries = DatabaseUtils.createQueries_numberObjectBetweenShowColumnInColumn(dbConnection.getConnection(), dbConnection.getDatabaseName(), object1, object2, showname, columnname);
                        } else {
                            for (String table: DatabaseUtils.getTables(dbConnection.getConnection(), dbConnection.getDatabaseName())) {
                                queries = DatabaseUtils.createQueries_numberObjectBetweenShowColumnInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), object1, object2, showname,table);
                                processQueries(queries);
                            }
                            return;
                        }
                    }
                }

                processQueries(queries);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            if (map.containsKey("obj1")) {
                objectname = map.get("obj1");
            } else if (map.containsKey("obj2")) {
                objectname = map.get("obj2");
            } else {
                objectname = map.get("obj");
            }

            try{
                List<String> queries;

                if (showname == null) {
                    if (tablename != null) {
                        if (columnname != null) {
                            queries = DatabaseUtils.createQueries_numberObjectInColumnInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), objectname, columnname, tablename);
                        } else {
                            queries = DatabaseUtils.createQueries_numberObjectInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), objectname, tablename);
                        }
                    } else {
                        if (columnname != null) {
                            queries = DatabaseUtils.createQueries_numberObjectInColumn(dbConnection.getConnection(), dbConnection.getDatabaseName(), objectname, columnname);
                        } else {
                            for (String table: DatabaseUtils.getTables(dbConnection.getConnection(), dbConnection.getDatabaseName())) {
                                queries = DatabaseUtils.createQueries_numberObjectInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), objectname, table);
                                processQueries(queries);
                            }
                            return;
                        }
                    }
                } else {
                    if (tablename != null) {
                        if (columnname != null) {
                            queries = DatabaseUtils.createQueries_numberObjectShowColumnInColumnInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), objectname, showname, columnname, tablename);
                        } else {
                            queries = DatabaseUtils.createQueries_numberObjectShowColumnInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), objectname, showname,tablename);
                        }
                    } else {
                        if (columnname != null) {
                            queries = DatabaseUtils.createQueries_numberObjectShowColumnInColumn(dbConnection.getConnection(), dbConnection.getDatabaseName(), objectname, showname, columnname);
                        } else {
                            for (String table: DatabaseUtils.getTables(dbConnection.getConnection(), dbConnection.getDatabaseName())) {
                                queries = DatabaseUtils.createQueries_numberObjectShowColumnInTable(dbConnection.getConnection(), dbConnection.getDatabaseName(), objectname, showname, table);
                                processQueries(queries);
                            }
                            return;

                        }
                    }
                }

                processQueries(queries);

            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

    }

    private Map<String, String> getTableColumn(Map<String, String> map) {
        String tablename = null;
        String columnname = null;
        String showname = null;

        boolean next = false;
        if (map.containsKey("table")) {

            List<String> tables = searchTableName(map.get("table"));

            if (tables != null && !tables.isEmpty()) {
                if (tables.size() == 1) {
                    tablename = tables.get(0);
                } else {
                    System.out.println(tables);
                }
            }
        }

        if (map.containsKey("col")) {
            if (tablename == null) {
                List <String> tables = searchTableName(map.get("col"));

                if (tables != null && !tables.isEmpty()) {
                    if (tables.size() == 1) {
                        tablename = tables.get(0);
                    } else {
                        System.out.println(tables);
                    }
                } else {
                    next = true;
                }
            } else {
                next = true;
            }

            if (next) {
                List <String> columns = searchColumnName(map.get("col"), tablename);

                if (columns != null && !columns.isEmpty()) {
                    if (columns.size() == 1) {
                        columnname = columns.get(0);
                    } else {
                        System.out.println(columns);
                    }
                }
            }

        }
        next = false;
        if (map.containsKey("show")) {
            if (tablename == null) {
                List <String> tables = searchTableName(map.get("show"));

                if (tables != null && !tables.isEmpty()) {
                    if (tables.size() == 1) {
                        tablename = tables.get(0);
                    } else {
                        //System.out.println(tables);
                    }
                } else {
                    next = true;
                }
            } else {
                next = true;
            }

            if (next) {
                List <String> columns = searchColumnName(map.get("show"), tablename);

                if (columns != null && !columns.isEmpty()) {
                    if (columns.size() == 1) {
                        showname = columns.get(0);
                    } else {
                        //System.out.println(columns);
                    }
                }
            }
        }
        Map<String, String> out = new HashMap<>();
        out.put("table", tablename);
        out.put("column", columnname);

        if (map.containsKey("obj")) {
            if (showname == map.get("obj")) {
                showname = null;
            }
            try {
                Integer.parseInt(map.get("obj"));
                map.put("obj", "="+map.get("obj"));
            } catch (NumberFormatException ignored) {
            }
        }

        out.put("show", showname);
        return out;
    }

    private List<String> searchTableName(String name) {
        String searchString = "%"+name+"%";
        if (searchString.contains(" "))
            searchString = searchString.replace(' ', '%');
        List<String> tables = DatabaseUtils.getTables(dbConnection.getConnection(), name);
        if (tables == null || tables.isEmpty()) {
            tables = DatabaseUtils.searchForTablesLike(dbConnection.getConnection(), dbConnection.getDatabaseName(), searchString);
        }

        return tables;
    }

    private List<String> searchColumnName(String name, String tablename) {
        String searchString = "%"+name+"%";
        if (searchString.contains(" "))
            searchString = searchString.replace(' ', '%');

        List <String> columns = new ArrayList<>();

        if (tablename != null) {
            List<String> tmp_columns = DatabaseUtils.getColumn(dbConnection.getConnection(), dbConnection.getDatabaseName(), name, tablename);
            if (tmp_columns != null && !tmp_columns.isEmpty()) {
                columns.addAll(tmp_columns);
            } else {
                tmp_columns = DatabaseUtils.getColumnsLike(dbConnection.getConnection(), dbConnection.getDatabaseName(), searchString, tablename);
            } if (tmp_columns != null && !tmp_columns.isEmpty()) {
                columns.addAll(tmp_columns);
            }

            if (!columns.isEmpty()) {
                return columns;
            }
        }

        List<String> tables = DatabaseUtils.getTablesFromColumnLike(dbConnection.getConnection(), dbConnection.getDatabaseName(), searchString);
        System.out.println(tables);
        if (tables != null && tables.size() == 1) {
            columns = DatabaseUtils.getColumn(dbConnection.getConnection(), dbConnection.getDatabaseName(), name, tables.get(0));
        } else if (tables != null){
            for (String table: tables) {
                List<String> columnsFound = DatabaseUtils.getColumnsLike(dbConnection.getConnection(), dbConnection.getDatabaseName(), searchString,table);

                if (columnsFound != null)
                    columns.addAll(columnsFound);
            }
        }
        if (columns == null || columns.isEmpty()) {
            columns = DatabaseUtils.searchForColumns(dbConnection.getConnection(), dbConnection.getDatabaseName(), searchString);
        }
        return columns;
    }

    private void processQueries(List<String> queries) {
        if (queries != null) {
            System.out.println(queries);
            System.out.println("\n---------- OUTPUT ----------\n");
            List<Row> rows = DatabaseUtils.RunAllQueries(dbConnection.getConnection(), queries);
            for (Row row : rows)
            {
                for (Map.Entry<Object, Class> col: row.row)
                {
                    System.out.print(" > " + ((col.getValue()).cast(col.getKey())));
                }
                System.out.println();
            }
            System.out.println("\n----------- END -----------\n");
        }
    }




}
