/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista.ComercialComprasyVentas;

import javax.swing.JOptionPane;
import Controlador.Compras.clsFacturadetallecompras;
import Controlador.Compras.clsFacturascompras;
import Modelo.Compras.FacturascomprasDAO;
import Modelo.Compras.FacturadetallecomprasDAO;
import java.io.File;
import javax.swing.table.DefaultTableModel;
import Modelo.BitacoraDAO;
import Controlador.clsUsuarioConectado;
import Modelo.Conexion;
import Modelo.PermisosDAO;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import Vista.Logistica.frmMantenimientoProductos;
import java.awt.Dimension;
/**
 *
 * @author Isaias Cedillo
 */
public class frmCompras extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(frmCompras.class.getName());
    private static final int APL_CODIGO = 3001;
    /**
     * Creates new form frmCompras
     */
    public frmCompras() {
        initComponents();
        cargarTablaDetalles();
        cargarTablaFacturas();
        cargarComboProveedores();
        cargarComboProductos();
        cargarPermisos();
    }
     public void cargarPermisos() {
    int usuId = clsUsuarioConectado.getUsuId();
    PermisosDAO permisosDAO = new PermisosDAO();

    //METODO PARA EL SISTEMA DE SEGURIDAD DE PERMISOS AGREGAR A SUS FORMULARIOS CORRESPONDIENTES
    // Todos usan código 10 = Mantenimiento Usuario
    Insert.setEnabled( permisosDAO.puedeInsertar (usuId, 3000) );
    
    Query.setEnabled  ( permisosDAO.puedeBuscar   (usuId, 3000) );
    //actualizar
    Update.setEnabled( permisosDAO.puedeModificar(usuId, 3000) );
    //delete
    Delete.setEnabled( permisosDAO.puedeEliminar(usuId, 3000) );
    //Reporte
    jButton1.setEnabled( permisosDAO.puedeReportar (usuId, 3000) );
}
     
    private void cargarTablaFacturas() {

    DefaultTableModel modelo = new DefaultTableModel();

    modelo.addColumn("ID");
    modelo.addColumn("Número");
    modelo.addColumn("Fecha");
    modelo.addColumn("Proveedor");
    modelo.addColumn("Subtotal");
    modelo.addColumn("IVA");
    modelo.addColumn("Total");
    modelo.addColumn("Estado");

    FacturascomprasDAO dao = new FacturascomprasDAO();

    for (clsFacturascompras factura : dao.listar()) {
        modelo.addRow(new Object[]{
            factura.getFaccomid(),
            factura.getFaccomnumero(),
            factura.getFaccomfecha(),
            factura.getProcodigo(),
            String.format("%.2f", factura.getFaccomsubtotal()),
            String.format("%.2f", factura.getFaccomiva()),
            String.format("%.2f", factura.getFaccomtotal()),
            factura.getFaccomestado()
        });
    }

    tablafacturascompras.setModel(modelo);
}
private void cargarTablaDetalles(int idFactura) {

    DefaultTableModel modelo = new DefaultTableModel();

    modelo.addColumn("DetalleID");
    modelo.addColumn("ProductoID");
    modelo.addColumn("Cantidad");
    modelo.addColumn("Precio");
    modelo.addColumn("Subtotal");

    FacturadetallecomprasDAO dao =
            new FacturadetallecomprasDAO();

    for (clsFacturadetallecompras detalle : dao.listarPorFactura(idFactura)) {

        modelo.addRow(new Object[]{
            detalle.getFaccomdetid(),
            detalle.getProdid(),
            detalle.getFaccomcantidad(),
            String.format("%.2f", detalle.getFaccomprecio()),
            String.format("%.2f", detalle.getFaccomsubtotal())
        });
    }

    facturadetallescompras.setModel(modelo);
}
private void cargarTablaDetalles() {

    DefaultTableModel modelo = new DefaultTableModel();

    modelo.addColumn("DetalleID");
    modelo.addColumn("FacturaID");
    modelo.addColumn("ProductoID");
    modelo.addColumn("Cantidad");
    modelo.addColumn("Precio");
    modelo.addColumn("Subtotal");

    FacturadetallecomprasDAO dao =
            new FacturadetallecomprasDAO();

    for (clsFacturadetallecompras detalle : dao.listar()) {

        modelo.addRow(new Object[]{
            detalle.getFaccomdetid(),
            detalle.getFaccomid(),
            detalle.getProdid(),
            String.format("%.2f", detalle.getFaccomcantidad()),
            String.format("%.2f", detalle.getFaccomprecio()),
            String.format("%.2f", detalle.getFaccomsubtotal())
        });
    }

    facturadetallescompras.setModel(modelo);
}
    private void limpiarCampos() {
    Faccomnumero_txt.setText("");
    Faccomcantidad_txt.setText("");
    Faccomprecio_txt.setText("");
    Faccomsubtotal_txt.setText("");
    Faccomiva_txt.setText("");
    Faccomtotal_txt.setText("");
}
   private void registrarBitacora(String accion) {
    try {
        new BitacoraDAO().insert(
                clsUsuarioConectado.getUsuId(),
                APL_CODIGO,
                accion
        );
    } catch (Exception e) {
        e.printStackTrace();
    }
} 
   private void cargarComboProveedores() {

    comboProveedor.removeAllItems();

    String sql = "SELECT Procodigo, Pronombre FROM proveedores";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            int id = rs.getInt("Procodigo");
            String nombre = rs.getString("Pronombre");

            comboProveedor.addItem(id + " - " + nombre);
        }

    } catch (Exception e) {
        e.printStackTrace();

        JOptionPane.showMessageDialog(
                null,
                "Error al cargar proveedores: " + e.getMessage()
        );
    }
}
 private void cargarComboProductos() {

    comboProducto.removeAllItems();

    String sql = "SELECT Prodid, Prodnombre FROM productos";

    try (Connection conn = Conexion.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            int id = rs.getInt("Prodid");
            String nombre = rs.getString("Prodnombre");

            comboProducto.addItem(id + " - " + nombre);
        }

    } catch (Exception e) {
        e.printStackTrace();

        JOptionPane.showMessageDialog(
                null,
                "Error al cargar productos: " + e.getMessage()
        );
    }
}  
 private int obtenerIdCombo(javax.swing.JComboBox<String> combo) {

    String seleccionado = (String) combo.getSelectedItem();

    if (seleccionado == null || seleccionado.trim().isEmpty()) {
        throw new RuntimeException("Debe seleccionar un dato del combo");
    }

    String[] partes = seleccionado.split(" - ");

    return Integer.parseInt(partes[0].trim());
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        Faccomid_txt = new javax.swing.JTextField();
        Faccomsubtotal_txt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        Faccomiva_txt = new javax.swing.JTextField();
        Query = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        Update = new javax.swing.JButton();
        Faccomtotal_txt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        facturadetallescompras = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        Faccomdetid_txt = new javax.swing.JTextField();
        Faccomestado_txt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        help = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablafacturascompras = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        Faccomprecio_txt = new javax.swing.JTextField();
        Faccomcantidad_txt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        Calculo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        Insert = new javax.swing.JButton();
        Faccomnumero_txt = new javax.swing.JTextField();
        Delete = new javax.swing.JButton();
        Clean = new javax.swing.JButton();
        Exit = new javax.swing.JButton();
        comboProducto = new javax.swing.JComboBox<>();
        comboProveedor = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel3.setText("Subtotal");

        Faccomsubtotal_txt.setEditable(false);
        Faccomsubtotal_txt.setBackground(new java.awt.Color(204, 204, 204));

        jLabel13.setText("Busqueda ID");

        Faccomiva_txt.setEditable(false);
        Faccomiva_txt.setBackground(new java.awt.Color(204, 204, 204));

        Query.setText("Buscar");
        Query.addActionListener(this::QueryActionPerformed);

        jLabel4.setText("Iva");

        Update.setText("Actualizar");
        Update.addActionListener(this::UpdateActionPerformed);

        Faccomtotal_txt.setEditable(false);
        Faccomtotal_txt.setBackground(new java.awt.Color(204, 204, 204));

        jLabel5.setText("Total");

        facturadetallescompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id detalles", "Producto", "Cantidad", "Precio", "Subtotal"
            }
        ));
        jScrollPane2.setViewportView(facturadetallescompras);

        jLabel6.setText("Estado");

        Faccomdetid_txt.addActionListener(this::Faccomdetid_txtActionPerformed);

        jLabel14.setText("Busqueda ID detalles");

        help.setBackground(new java.awt.Color(255, 255, 102));
        help.setText("Help");
        help.addActionListener(this::helpActionPerformed);

        tablafacturascompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id Factura", "No. Factura", "Proveedor Code", "Subtotal", "IVA", "Total", "Estado"
            }
        ));
        jScrollPane1.setViewportView(tablafacturascompras);

        jButton1.setText("Reporte");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jLabel8.setText("DETALLES");

        jLabel9.setText("Precio Unitario de producto");

        jLabel10.setText("Cantidad de producto");

        jLabel12.setText("Id del producto");

        jLabel1.setText("Id de Proveedor");

        Calculo.setText("Calcular gastos");
        Calculo.addActionListener(this::CalculoActionPerformed);

        jLabel2.setText("Numero de factura");

        Insert.setText("Registrar");
        Insert.setEnabled(false);
        Insert.addActionListener(this::InsertActionPerformed);

        Delete.setText("Eliminar");
        Delete.addActionListener(this::DeleteActionPerformed);

        Clean.setText("Limpiar");
        Clean.addActionListener(this::CleanActionPerformed);

        Exit.setText("Salir");
        Exit.addActionListener(this::ExitActionPerformed);

        comboProducto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboProducto.addActionListener(this::comboProductoActionPerformed);

        comboProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboProveedor.addActionListener(this::comboProveedorActionPerformed);

        jLabel7.setText("Revisar el precio de productos en su tabla");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(Clean)
                        .addGap(18, 18, 18)
                        .addComponent(Exit))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel12)
                                .addGap(28, 28, 28)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(comboProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Faccomestado_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(128, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(46, 46, 46)
                .addComponent(jLabel7)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Faccomiva_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Faccomsubtotal_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(Faccomnumero_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Faccomtotal_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(Calculo))
                            .addGap(18, 18, 18)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 545, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(help)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton1)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabel10)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(Faccomcantidad_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addGap(18, 18, 18)
                                            .addComponent(Faccomprecio_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(Insert)
                                            .addGap(12, 12, 12)
                                            .addComponent(Delete))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(Query)
                                            .addGap(18, 18, 18)
                                            .addComponent(Update, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(Faccomid_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel13)
                                        .addComponent(Faccomdetid_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel14))))
                            .addGap(86, 86, 86)))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(Faccomestado_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 196, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addGap(91, 91, 91)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12))
                        .addGap(62, 62, 62)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(Clean)
                            .addComponent(Exit)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(33, 33, 33)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(Faccomnumero_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Faccomsubtotal_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel5))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(Faccomiva_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(Faccomtotal_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(28, 28, 28)
                            .addComponent(Calculo)
                            .addGap(70, 70, 70)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel9)
                                .addComponent(Faccomprecio_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel10)
                                .addComponent(Faccomcantidad_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel13)
                            .addGap(2, 2, 2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(Insert)
                                .addComponent(Delete)
                                .addComponent(Faccomid_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(Query)
                                        .addComponent(Update)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(5, 5, 5)
                                    .addComponent(jLabel14)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(Faccomdetid_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGap(66, 66, 66)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(help)
                        .addComponent(jButton1))
                    .addContainerGap(62, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void QueryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QueryActionPerformed
        // TODO add your handling code here:

        try {

            // Validar vacío
            if (Faccomid_txt.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                    null,
                    "Ingrese un ID");

                return;
            }

            // Obtener ID
            int idFactura =
            Integer.parseInt(
                Faccomid_txt.getText().trim());

            // DAO
            FacturascomprasDAO dao =
            new FacturascomprasDAO();

            // Buscar
            clsFacturascompras factura =
            dao.query(idFactura);

            // Verificar resultado
            if (factura != null) {

                Faccomnumero_txt.setText(
                    factura.getFaccomnumero());

                Faccomsubtotal_txt.setText(
                    String.format("%.2f",
                        factura.getFaccomsubtotal()));

                Faccomiva_txt.setText(
                    String.format("%.2f",
                        factura.getFaccomiva()));

                Faccomtotal_txt.setText(
                    String.format("%.2f",
                        factura.getFaccomtotal()));

                Faccomestado_txt.setText(
                    factura.getFaccomestado());

                JOptionPane.showMessageDialog(
                    null,
                    "Factura encontrada");
                   registrarBitacora("QUERY:" + idFactura);
            } else {

                JOptionPane.showMessageDialog(
                    null,
                    "Factura no encontrada");

                limpiarCampos();
            }

        }

        catch(NumberFormatException e){

            JOptionPane.showMessageDialog(
                null,
                "Ingrese un ID válido");
        }

        catch(Exception e){

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                null,
                e.getMessage());
        }
    }//GEN-LAST:event_QueryActionPerformed

    private void UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UpdateActionPerformed
        // TODO add your handling code here:
        try {
            // =========================
            // VALIDAR CAMPOS VACÍOS
            // =========================
            if (Faccomid_txt.getText().trim().isEmpty()
                || Faccomdetid_txt.getText().trim().isEmpty()
                || Faccomnumero_txt.getText().trim().isEmpty()
                || Faccomcantidad_txt.getText().trim().isEmpty()
                || Faccomprecio_txt.getText().trim().isEmpty()
                || Faccomestado_txt.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
                return;
            }

            // =========================
            // OBTENER DATOS
            // =========================
            int faccomid = Integer.parseInt(Faccomid_txt.getText().trim());
            int faccomdetid = Integer.parseInt(Faccomdetid_txt.getText().trim());
            double cantidad = Double.parseDouble(Faccomcantidad_txt.getText().trim());
            double precio = Double.parseDouble(Faccomprecio_txt.getText().trim());
            //=====================
            // IDs
            //=====================
            int procodigo = obtenerIdCombo(comboProveedor);
            int prodid = obtenerIdCombo(comboProducto);
            // =========================
            // CALCULAR TOTALES
            // =========================
            double subtotalDetalle = cantidad * precio;

            double subtotalFactura = subtotalDetalle;
            double iva = subtotalFactura * 0.12;
            double total = subtotalFactura + iva;

            Faccomsubtotal_txt.setText(String.format("%.2f", subtotalFactura));
            Faccomiva_txt.setText(String.format("%.2f", iva));
            Faccomtotal_txt.setText(String.format("%.2f", total));

            // =========================
            // OBJETO FACTURA
            // =========================
            clsFacturascompras factura = new clsFacturascompras();

            factura.setFaccomid(faccomid);
            factura.setFaccomnumero(Faccomnumero_txt.getText().trim());
            factura.setFaccomsubtotal(subtotalFactura);
            factura.setFaccomiva(iva);
            factura.setFaccomtotal(total);
            factura.setFaccomestado(Faccomestado_txt.getText().trim());
            factura.setProcodigo(procodigo);

            // =========================
            // OBJETO DETALLE
            // =========================
            clsFacturadetallecompras detalle = new clsFacturadetallecompras();

            detalle.setFaccomdetid(faccomdetid);
            detalle.setFaccomid(faccomid);
            detalle.setFaccomcantidad(cantidad);
            detalle.setFaccomprecio(precio);
            detalle.setFaccomsubtotal(subtotalDetalle);
            detalle.setProdid(prodid);

            // =========================
            // ACTUALIZAR EN BD
            // =========================
            FacturascomprasDAO facturaDAO = new FacturascomprasDAO();
            FacturadetallecomprasDAO detalleDAO = new FacturadetallecomprasDAO();

            facturaDAO.update(factura);
            detalleDAO.update(detalle);

            JOptionPane.showMessageDialog(null, "Factura actualizada correctamente");
            registrarBitacora("UPDATE:" + faccomid);
            cargarTablaFacturas();
            cargarTablaDetalles();
            limpiarCampos();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(null, "Ingrese valores numéricos válidos");

        } catch (Exception e) {

            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
        }
    }//GEN-LAST:event_UpdateActionPerformed

    private void Faccomdetid_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Faccomdetid_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Faccomdetid_txtActionPerformed

    private void helpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpActionPerformed
        // TODO add your handling code here:
        try {
        File archivo = new File(
            "src\\main\\java\\Ayudas\\Compras\\AyudaComprasHelp.chm"
        );

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(
                null,
                "La ayuda no fue encontrada en:\n" + archivo.getAbsolutePath()
            );
            return;
        }

        ProcessBuilder pb = new ProcessBuilder(
            "cmd",
            "/c",
            "start",
            "",
            archivo.getAbsolutePath()
        );

        pb.start();

    } catch (Exception ex) {
        ex.printStackTrace();

        JOptionPane.showMessageDialog(
            null,
            "Error al abrir la ayuda:\n" + ex.getMessage()
        );
    }

    }//GEN-LAST:event_helpActionPerformed

    private void CalculoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CalculoActionPerformed
        // TODO add your handling code here:
        try {
            if(Faccomprecio_txt.getText().trim().isEmpty()||Faccomcantidad_txt.getText().trim().isEmpty())
            {
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacío");
                return;
            }
            double precio = Double.parseDouble(Faccomprecio_txt.getText());
            int cantidad = Integer.parseInt(Faccomcantidad_txt.getText());
            double subtotal = cantidad*precio;
            double iva = subtotal * 0.12;
            double total = subtotal + iva;
            Faccomsubtotal_txt.setText(String.format("%.2f", subtotal));
            Faccomiva_txt.setText(String.format("%.2f", iva));
            Faccomtotal_txt.setText(String.format("%.2f", total));
        }
        catch(NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "Ingrese números válidos");
        }

    }//GEN-LAST:event_CalculoActionPerformed

    private void InsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InsertActionPerformed

        try {
            // =========================
            // VALIDAR CAMPOS VACÍOS
            // =========================
            if (Faccomnumero_txt.getText().trim().isEmpty()
                || Faccomcantidad_txt.getText().trim().isEmpty()
                || Faccomprecio_txt.getText().trim().isEmpty()
                || Faccomestado_txt.getText().trim().isEmpty()){

                JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
                return;
            }

            // =========================
            // DATOS DEL DETALLE
            // =========================
            double cantidad = Double.parseDouble(Faccomcantidad_txt.getText().trim());
            double precio = Double.parseDouble(Faccomprecio_txt.getText().trim());

            double subtotalDetalle = cantidad * precio;
            // =========================
            // OBTENER IDS DESDE COMBOBOX
            // =========================
              int procodigo = obtenerIdCombo(comboProveedor);
              int prodid = obtenerIdCombo(comboProducto);
            // =========================
            // TOTALES DE FACTURA
            // =========================
            double subtotalFactura = subtotalDetalle;
            double iva = subtotalFactura * 0.12;
            double total = subtotalFactura + iva;

            // Mostrar totales en el formulario
            Faccomsubtotal_txt.setText(String.format("%.2f", subtotalFactura));
            Faccomiva_txt.setText(String.format("%.2f", iva));
            Faccomtotal_txt.setText(String.format("%.2f", total));

            // =========================
            // CREAR OBJETO FACTURA
            // =========================
            clsFacturascompras factura = new clsFacturascompras();

            factura.setFaccomnumero(Faccomnumero_txt.getText().trim());
            factura.setFaccomsubtotal(subtotalFactura);
            factura.setProcodigo(procodigo);
            factura.setFaccomiva(iva);
            factura.setFaccomtotal(total);
            factura.setFaccomestado(Faccomestado_txt.getText().trim());

            // =========================
            // INSERTAR FACTURA
            // =========================
            FacturascomprasDAO facturaDAO = new FacturascomprasDAO();

            int idFacturaGenerada = facturaDAO.insert(factura);

            if (idFacturaGenerada <= 0) {
                JOptionPane.showMessageDialog(null, "No se pudo generar la factura");
                return;
            }

            // =========================
            // CREAR OBJETO DETALLE
            // =========================
            clsFacturadetallecompras detalle = new clsFacturadetallecompras();

            detalle.setFaccomid(idFacturaGenerada);
            detalle.setFaccomcantidad(cantidad);
            detalle.setProdid(prodid);
            detalle.setFaccomprecio(precio);
            detalle.setFaccomsubtotal(subtotalDetalle);

            // =========================
            // INSERTAR DETALLE
            // =========================
            FacturadetallecomprasDAO detalleDAO = new FacturadetallecomprasDAO();

            detalleDAO.insert(detalle);

            JOptionPane.showMessageDialog(null, "Factura y detalle guardados correctamente");
            registrarBitacora("INSERT:" + idFacturaGenerada);

            // =========================
            // RECARGAR TABLA
            // =========================
            cargarTablaFacturas();
            cargarTablaDetalles();
            // Opcional: limpiar campos
            limpiarCampos();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(null, "Debe ingresar valores numéricos válidos");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Error al guardar la factura: " + e.getMessage());
            e.printStackTrace();
        }

    }//GEN-LAST:event_InsertActionPerformed

    private void DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteActionPerformed
        // TODO add your handling code here:
        try {

            // Validar ID
            if (Faccomid_txt.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(
                    null,
                    "Seleccione una factura");

                return;
            }

            int idFactura =
            Integer.parseInt(
                Faccomid_txt.getText().trim());

            // Confirmación
            int respuesta =
            JOptionPane.showConfirmDialog(
                null,
                "¿Desea eliminar la factura?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

            if (respuesta != JOptionPane.YES_OPTION) {
                return;
            }

            // DAO
            FacturadetallecomprasDAO detalleDAO =
            new FacturadetallecomprasDAO();

            FacturascomprasDAO facturaDAO =
            new FacturascomprasDAO();

            // Primero detalles
            detalleDAO.deleteByFactura(idFactura);

            // Luego factura
            facturaDAO.delete(idFactura);

            JOptionPane.showMessageDialog(
                null,
                "Factura eliminada");
            registrarBitacora("DELETE:" + idFactura);
            cargarTablaFacturas();
            cargarTablaDetalles();
            limpiarCampos();

        }

        catch(NumberFormatException e){

            JOptionPane.showMessageDialog(
                null,
                "ID inválido");
        }

        catch(Exception e){

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                null,
                e.getMessage());
        }
    }//GEN-LAST:event_DeleteActionPerformed

    private void CleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CleanActionPerformed
        // TODO add your handling code here:
        limpiarCampos();
        
    }//GEN-LAST:event_CleanActionPerformed

    private void ExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitActionPerformed
        // TODO add your handling code here:
        MdiComercio ventana = new MdiComercio(); 
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ExitActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
            try {
        Conexion cn = new Conexion();
        Connection con = cn.getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos.");
            return;
        }

        String rutaReporte = "src\\main\\java\\Reportes\\ComprayVentas\\reporteCompras.jasper";

        Map<String, Object> parametros = new HashMap<>();

        JasperPrint reporte = JasperFillManager.fillReport(
                rutaReporte,
                parametros,
                con
        );

        JasperViewer.viewReport(reporte, false);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
                null,
                "Error al generar el reporte:\n" + e.getMessage()
        );
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void comboProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboProductoActionPerformed

    private void comboProveedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboProveedorActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_comboProveedorActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new frmCompras().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Calculo;
    private javax.swing.JButton Clean;
    private javax.swing.JButton Delete;
    private javax.swing.JButton Exit;
    private javax.swing.JTextField Faccomcantidad_txt;
    private javax.swing.JTextField Faccomdetid_txt;
    private javax.swing.JTextField Faccomestado_txt;
    private javax.swing.JTextField Faccomid_txt;
    private javax.swing.JTextField Faccomiva_txt;
    private javax.swing.JTextField Faccomnumero_txt;
    private javax.swing.JTextField Faccomprecio_txt;
    private javax.swing.JTextField Faccomsubtotal_txt;
    private javax.swing.JTextField Faccomtotal_txt;
    private javax.swing.JButton Insert;
    private javax.swing.JButton Query;
    private javax.swing.JButton Update;
    private javax.swing.JComboBox<String> comboProducto;
    private javax.swing.JComboBox<String> comboProveedor;
    private javax.swing.JTable facturadetallescompras;
    private javax.swing.JButton help;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablafacturascompras;
    // End of variables declaration//GEN-END:variables
}
