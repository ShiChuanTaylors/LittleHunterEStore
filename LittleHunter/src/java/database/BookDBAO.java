package database;
import java.sql.*;
import javax.sql.*;
import javax.naming.*;
import java.util.*;
import exception.*;
import cart.*;
public class BookDBAO {
    private ArrayList shirts;
    Connection con;
    private boolean conFree = true;
    public BookDBAO() throws Exception {
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            DataSource ds = (DataSource) envCtx.lookup("jdbc/BookDB");
            con = ds.getConnection();
        } catch (Exception ex) {
            throw new Exception("Couldn't open connection to database: " +ex.getMessage());
        }
    }
    public void remove() {
        try {con.close(); } catch (SQLException ex) {System.out.println(ex.getMessage());}
    }
    protected synchronized Connection getConnection() {
        while (conFree == false) {
            try { wait(); } catch (InterruptedException e) {
                System.out.println("------2------");
            }
        }
        conFree = false;
        notify();
        return con;
    }
    protected synchronized void releaseConnection() {
        while (conFree == true) {
            try {wait(); } catch (InterruptedException e) {}
        }
        conFree = true;
        notify();
    }
    public List getBooks() throws BooksNotFoundException {
        System.out.print("-----3--------");
        shirts = new ArrayList();
        System.out.print("-----4--------");
        int inventory = 0;
        try {
            getConnection();
            PreparedStatement prepStmt = con.prepareStatement("select * from shirts");
            System.out.print("-----1--------");
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                inventory = rs.getInt(4);
                if (rs.getInt(4) > 0) {
                    shirts.add(new BookDetails(rs.getString(1), rs.getString(2),
                            rs.getFloat(3), rs.getInt(4), rs.getString(5),rs.getString(6)));
                }
            }
            //prepStmt.close();
            System.out.printf("Inventory: " + inventory);
        } 
        catch (SQLException ex) { 

            //throw new BooksNotFoundException(ex.getMessage()); 
            
        }
        finally {releaseConnection();}
        Collections.sort(shirts);
        return shirts;
    }
    public BookDetails getBookDetails(String bookId)throws BookNotFoundException {
        try {
            getConnection();
            PreparedStatement prepStmt = con.prepareStatement("select * from shirts where id = ? ");
            prepStmt.setString(1, bookId);
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                BookDetails bd = new BookDetails(rs.getString(1), rs.getString(2),
                            rs.getFloat(3), rs.getInt(4), rs.getString(5),rs.getString(6));
                //prepStmt.close();
                releaseConnection();
                return bd;
            } else {
                //prepStmt.close();
                releaseConnection();
                throw new BookNotFoundException("Couldn't find book: " +bookId);
            }
        } catch (SQLException ex) {
            releaseConnection();
            throw new BookNotFoundException("Couldn't find book: " + bookId +" " + ex.getMessage());
        }
    }
    public boolean deleteBook(String bookId)throws BookNotFoundException {
        try {
            getConnection();
            PreparedStatement prepStmt = con.prepareStatement("Delete from shirts where id = ? ");
            prepStmt.setString(1, bookId);
            prepStmt.executeUpdate();
            releaseConnection();
            return true;
        } catch (SQLException ex) {
            releaseConnection();
            throw new BookNotFoundException("Couldn't delete book: " + bookId +" " + ex.getMessage());
        }
    }
    public boolean addBook(String id, String title, String author,
        float price, int year,String description, int inventory)
            throws NewBookException{
        try {
            getConnection();
            PreparedStatement prepStmt = con.prepareStatement("INSERT INTO shirts VALUES(?,?,?,?,?,?,?)");
            prepStmt.setString(1,id);
            prepStmt.setString(2,title);
            prepStmt.setString(3,author);
            prepStmt.setFloat(4,price);
            prepStmt.setInt(5,year);
            prepStmt.setString(6,description);
            prepStmt.setInt(7,inventory);
            prepStmt.executeUpdate();
            releaseConnection();
            return true;
        } catch (SQLException ex) {
            releaseConnection();
            throw new NewBookException("Couldn't  Add new book due to\n"+ex.getMessage());
        }
    }
    public void buyBooks(ShoppingCart cart) throws OrderException {
        Collection items = cart.getItems();
        Iterator i = items.iterator();
        try {
            getConnection();
            con.setAutoCommit(false);
            while (i.hasNext()) {
                ShoppingCartItem sci = (ShoppingCartItem) i.next();
                BookDetails bd = (BookDetails) sci.getItem();
                String id = bd.getId();
                buyBook(id, sci.getQuantity());
            }
            con.commit();
            con.setAutoCommit(true);
            releaseConnection();
        } catch (Exception ex) {
            try {
                con.rollback();
                throw new OrderException("Transaction failed: " + ex.getMessage());
            } catch (SQLException sqx) {                
                throw new OrderException("Rollback failed: " + sqx.getMessage());
            }
            finally{ releaseConnection();}
        }
    }
    private void buyBook(String bookId, int quantity) throws OrderException {
        try {
            PreparedStatement prepStmt = con.prepareStatement("select * from shirts where id = ? ");
            prepStmt.setString(1, bookId);
            ResultSet rs = prepStmt.executeQuery();
            if (rs.next()) {
                int inventory = rs.getInt(7);
                //prepStmt.close();
                if ((inventory - quantity) >= 0) {
                    prepStmt = con.prepareStatement("Update shirts set inventory = inventory - ? where id = ?");
                    prepStmt.setInt(1, quantity);
                    prepStmt.setString(2, bookId);
                    prepStmt.executeUpdate();
                    //prepStmt.close();
                } else  throw new OrderException("Not enough of " + bookId +" in stock to complete order.");
            }
        } catch (Exception ex) {
            throw new OrderException("Couldn't purchase book: " + bookId + ex.getMessage());
        }
    }
}