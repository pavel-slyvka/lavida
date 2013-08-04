import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * TestSwingMain
 * <p/>
 * Created: 20:09 03.08.13
 *
 * @author Pavel
 */
public class TestSwingMain extends JFrame {
    private static JTable table;

    public static void main(String[] args) {
        TestSwingMain main = new TestSwingMain();
        main.setSize(600, 600);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = main.getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(Color.LIGHT_GRAY);

        table = new JTable(20, 5);
        container.add(table);
        List<ProductView> productViewList = new ArrayList<ProductView>();
        productViewList.add(new ProductView("code1", "name1", 100.0, 300.0, 1));
        productViewList.add(new ProductView("code2", "name2", 110.0, 310.0, 2));
        productViewList.add(new ProductView("code3", "name3", 120.0, 320.0, 3));
        productViewList.add(new ProductView("code4", "name4", 130.0, 330.0, 4));
        productViewList.add(new ProductView("code5", "name5", 140.0, 340.0, 5));
        TableModel tableModel = new MyTableModel(productViewList);
        table.setModel(tableModel);

        main.setVisible(true);
    }

    public static class ProductView {
        String code;
        String name;
        Double cost;
        Double price;
        int count;

        public ProductView(String code, String name, Double cost, Double price, int count) {
            this.code = code;
            this.name = name;
            this.cost = cost;
            this.price = price;
            this.count = count;
        }
    }

    public static class MyTableModel implements TableModel {
        private Set<TableModelListener> listeners = new HashSet<TableModelListener>();
        private List<String> headers = Arrays.asList("Code", "Name", "Cost", "Price", "Count");
        private List<ProductView> data;

        public MyTableModel(List<ProductView> data) {
            this.data = data;
        }

        @Override
        public int getColumnCount() {
            return 5;
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public String getColumnName(int columnIndex) {
            return headers.get(columnIndex);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            ProductView productView = data.get(rowIndex);
            if (columnIndex == 0) return productView.code;
            else if (columnIndex == 1) return productView.name;
            else if (columnIndex == 2) return productView.cost;
            else if (columnIndex == 3) return productView.price;
            else if (columnIndex == 4) return productView.count;
            else return "";
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            ProductView productView = data.get(rowIndex-1);
            if (columnIndex == 0) productView.code = (String) aValue;
            else if (columnIndex == 1) productView.name = (String) aValue;
            else if (columnIndex == 2) productView.cost = Double.parseDouble((String) aValue);
            else if (columnIndex == 3) productView.price = Double.parseDouble((String) aValue);
            else if (columnIndex == 4) productView.count = Integer.parseInt((String) aValue);
            data.remove(2);
            table.updateUI();
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            listeners.add(l);
        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            listeners.remove(l);
        }
    }
}
