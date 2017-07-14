package database;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;

public class Row
{
    public List<Map.Entry<Object, Class>> row;
    public static Map<String, Class> TYPE;

    static
    {
        TYPE = new HashMap<String, Class>();

        TYPE.put("INTEGER", Integer.class);
        TYPE.put("TINYINT", Byte.class);
        TYPE.put("SMALLINT", Short.class);
        TYPE.put("BIGINT", Long.class);
        TYPE.put("REAL", Float.class);
        TYPE.put("FLOAT", Double.class);
        TYPE.put("DOUBLE", Double.class);
        TYPE.put("DECIMAL", BigDecimal.class);
        TYPE.put("NUMERIC", BigDecimal.class);
        TYPE.put("BOOLEAN", Boolean.class);
        TYPE.put("CHAR", String.class);
        TYPE.put("VARCHAR", String.class);
        TYPE.put("LONGVARCHAR", String.class);
        TYPE.put("DATE", Date.class);
        TYPE.put("TIME", Time.class);
        TYPE.put("TIMESTAMP", Timestamp.class);
        TYPE.put("SERIAL",Integer.class);
        // ...
    }

    public Row ()
    {
        row = new ArrayList<Map.Entry<Object, Class>>();
    }

    public <T> void add (T data)
    {
        row.add(new AbstractMap.SimpleImmutableEntry<Object,Class>(data, data.getClass()));
    }

    public void addNull (Class T) {
        row.add(new AbstractMap.SimpleImmutableEntry<Object, Class>(null, T));
    }

    public void add (Object data, String sqlType)
    {
        Class castType = Row.TYPE.get(sqlType);
        try {
            this.add(castType.cast(data));
        } catch (NullPointerException e) {
            this.addNull(castType);
        }
    }

    public static void formTable (ResultSet rs, ArrayList<Row> table) throws SQLException {
        if (rs == null) return;

        ResultSetMetaData rsmd = rs.getMetaData();

        int NumOfCol = rsmd.getColumnCount();

        while (rs.next())
        {
            Row row = new Row ();

            for(int i = 1; i <= NumOfCol; i++)
            {
                row.add(rs.getObject(i), rsmd.getColumnTypeName(i));
            }

            table.add(row);
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        for (Map.Entry<Object, Class> col: row)
        {
            out.append(" > ").append((col.getValue()).cast(col.getKey()));
        }
        return out.toString();
    }
}
