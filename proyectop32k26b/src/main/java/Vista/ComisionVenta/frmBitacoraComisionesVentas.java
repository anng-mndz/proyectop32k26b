/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Vista.ComisionVenta;
import Controlador.ComisionesVentas.clsBitacoraComisionesVenta;
import Modelo.ComisionesVentas.BitacoraComisionesDAO;
import Modelo.PermisosDAO;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import java.io.File;
/**
 *
 * @author Jorge Reyes
 */
public class frmBitacoraComisionesVentas extends javax.swing.JInternalFrame {
int idUsuario = Controlador.clsUsuarioConectado.getUsuId(); //este se mandó a llamar del clsUsuarioConectado
    private static final int Aplcodigo = 7000; //Codigo de aplicacion dado en clase para bitacora

    /**
     * Creates new form frmBitacoraComisionesVentas
     */
    public frmBitacoraComisionesVentas() {
        initComponents();
        llenadoDeTabla();

        // Ocultar campos al inicio
        txtBuscar.setVisible(false);
        fechaInicio.setVisible(false);
        fechaFin.setVisible(false);
        btnBuscar.setVisible(false);
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Bitácora Comisiones");
        setVisible(true);
        //Deben agregar esta línea para cargar el metodo 
        //cargarPermisos();
        
        cboxTipoBusquedaActionPerformed(null);
        BitacoraComisionesDAO bitacoraDAO = new BitacoraComisionesDAO();    
    bitacoraDAO.insert(idUsuario, Aplcodigo, "CONSULTA");
    }

    public void cargarPermisos() {
    PermisosDAO permisosDAO = new PermisosDAO();

    boolean puedeBuscar = permisosDAO.puedeBuscar(idUsuario, 7000);

    btnBuscar.setEnabled(puedeBuscar);
    cboxTipoBusqueda.setEnabled(puedeBuscar);
    }

    // Llenado de tabla
    // Obtiene todos los registros de la bitácora desde la base de datos
    // Limpia la tabla antes de llenarla
    // Recorre la lista de bitácoras y agrega cada registro al JTable
    public void llenadoDeTabla() {
    DefaultTableModel modelo = (DefaultTableModel) tablaBitacoraComisiones.getModel();
    modelo.setRowCount(0);

    BitacoraComisionesDAO bitacoracomisionesDAO = new BitacoraComisionesDAO();
    List<clsBitacoraComisionesVenta> bitacoras = bitacoracomisionesDAO.select();

    // Bucle for-each: más limpio, eficiente y evita usar bitacoras.get(i) continuamente
    for (clsBitacoraComisionesVenta bitacora : bitacoras) {
        // Se crea un arreglo nuevo por cada registro para evitar datos duplicados
        String[] dato = new String[7];
        
        dato[0] = Integer.toString(bitacora.getBitcodigo());
        dato[1] = Integer.toString(bitacora.getUsucodigo());
        dato[2] = Integer.toString(bitacora.getAplcodigo());
        dato[3] = bitacora.getBitfecha(); 
        dato[4] = bitacora.getBitip();
        dato[5] = bitacora.getBitequipo();
        dato[6] = bitacora.getBitaccion();
        
        System.out.println("Agregando a tabla: " + dato[0] + " - " + dato[6]);
        
        modelo.addRow(dato);
    }
}

    public void buscarPorCodigo() {
        clsBitacoraComisionesVenta bitacora = new clsBitacoraComisionesVenta();
        BitacoraComisionesDAO bitacoraDAO = new BitacoraComisionesDAO();
        bitacora.setBitcodigo(Integer.parseInt(txtBuscar.getText()));
        bitacora = bitacoraDAO.queryPorCodigo(bitacora);

        DefaultTableModel modelo = (DefaultTableModel) tablaBitacoraComisiones.getModel();
        modelo.setRowCount(0);

        if (bitacora != null) {
            String[] dato = new String[7];
            dato[0] = Integer.toString(bitacora.getBitcodigo());
            dato[1] = Integer.toString(bitacora.getUsucodigo());
            dato[2] = Integer.toString(bitacora.getAplcodigo());
            dato[3] = bitacora.getBitfecha().toString();
            dato[4] = bitacora.getBitip();
            dato[5] = bitacora.getBitequipo();
            dato[6] = bitacora.getBitaccion();
            modelo.addRow(dato);
        } else {
            javax.swing.JOptionPane.showMessageDialog(this,
                "No se encontró el registro",
                "Advertencia",
                javax.swing.JOptionPane.WARNING_MESSAGE);
        }
    }
    // Búsqueda por usuario
    // Busca todos los registros de bitácora de un usuario específico
    // Muestra los resultados en la tabla
    public void buscarPorUsuario() {
        BitacoraComisionesDAO bitacoraDAO = new BitacoraComisionesDAO();
        List<clsBitacoraComisionesVenta> bitacoras = bitacoraDAO.queryPorUsuario(
                Integer.parseInt(txtBuscar.getText()));

        DefaultTableModel modelo = (DefaultTableModel) tablaBitacoraComisiones.getModel();
        modelo.setRowCount(0);

        if (bitacoras.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "No se encontraron registros para ese usuario",
                "Advertencia",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] dato = new String[7];
        for (int i = 0; i < bitacoras.size(); i++) {
            dato[0] = Integer.toString(bitacoras.get(i).getBitcodigo());
            dato[1] = Integer.toString(bitacoras.get(i).getUsucodigo());
            dato[2] = Integer.toString(bitacoras.get(i).getAplcodigo());
            dato[3] = bitacoras.get(i).getBitfecha(); 
            dato[4] = bitacoras.get(i).getBitip();
            dato[5] = bitacoras.get(i).getBitequipo();
            dato[6] = bitacoras.get(i).getBitaccion();
            modelo.addRow(dato);
        }
    }
    // Búsqueda por aplicación
    // Busca registros de bitácora según la aplicación utilizada
    // Muestra los resultados en la tabla
    public void buscarPorAplicacion() {
        BitacoraComisionesDAO bitacoraDAO = new BitacoraComisionesDAO();
        List<clsBitacoraComisionesVenta> bitacoras = bitacoraDAO.queryPorAplicacion(
                Integer.parseInt(txtBuscar.getText()));

        DefaultTableModel modelo = (DefaultTableModel) tablaBitacoraComisiones.getModel();
        modelo.setRowCount(0);

        if (bitacoras.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "No se encontraron registros para esa aplicación",
                "Advertencia",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] dato = new String[7];
        for (int i = 0; i < bitacoras.size(); i++) {
            dato[0] = Integer.toString(bitacoras.get(i).getBitcodigo());
            dato[1] = Integer.toString(bitacoras.get(i).getUsucodigo());
            dato[2] = Integer.toString(bitacoras.get(i).getAplcodigo());
            dato[3] = bitacoras.get(i).getBitfecha(); 
            dato[4] = bitacoras.get(i).getBitip();
            dato[5] = bitacoras.get(i).getBitequipo();
            dato[6] = bitacoras.get(i).getBitaccion();
            modelo.addRow(dato);
        }
    }
     // Búsqueda por acción
    // Busca registros de bitácora según la acción realizada
    // Ejemplo: INSERT, UPDATE, DELETE
    public void buscarPorAccion() {
        BitacoraComisionesDAO bitacoraDAO = new BitacoraComisionesDAO();
        List<clsBitacoraComisionesVenta> bitacoras = bitacoraDAO.queryPorAccion(txtBuscar.getText());

        DefaultTableModel modelo = (DefaultTableModel) tablaBitacoraComisiones.getModel();
        modelo.setRowCount(0);

        if (bitacoras.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "No se encontraron registros para esa acción",
                "Advertencia",
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] dato = new String[7];
        for (int i = 0; i < bitacoras.size(); i++) {
            dato[0] = Integer.toString(bitacoras.get(i).getBitcodigo());
            dato[1] = Integer.toString(bitacoras.get(i).getUsucodigo());
            dato[2] = Integer.toString(bitacoras.get(i).getAplcodigo());
            dato[3] = bitacoras.get(i).getBitfecha(); 
            dato[4] = bitacoras.get(i).getBitip();
            dato[5] = bitacoras.get(i).getBitequipo();
            dato[6] = bitacoras.get(i).getBitaccion();
            modelo.addRow(dato);
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fechaFin = new com.toedter.calendar.JDateChooser();
        fechaInicio = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        cboxTipoBusqueda = new javax.swing.JComboBox<>();
        jButton2 = new javax.swing.JButton();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaBitacoraComisiones = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel3.setText("Bitácora Comisiones: ");

        cboxTipoBusqueda.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboxTipoBusqueda.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Código", "Usuario", "Aplicación", "Accion", "Rango de Fechas" }));
        cboxTipoBusqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboxTipoBusquedaActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton2.setText("Ayuda");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        txtBuscar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        btnBuscar.setBackground(new java.awt.Color(255, 227, 250));
        btnBuscar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnLimpiar.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        tablaBitacoraComisiones.setBackground(new java.awt.Color(51, 255, 255));
        tablaBitacoraComisiones.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Usuario", "Aplicación", "Fecha", "IP", "Equipo", "Acción"
            }
        ));
        jScrollPane2.setViewportView(tablaBitacoraComisiones);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Fecha Inicio:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Fecha Fin:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(43, 248, Short.MAX_VALUE)
                        .addComponent(btnBuscar)
                        .addGap(47, 47, 47)
                        .addComponent(btnLimpiar)
                        .addGap(30, 30, 30))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(124, 124, 124))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(14, 14, 14))))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cboxTipoBusqueda, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtBuscar))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(266, 266, 266)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(35, 35, 35)
                .addComponent(cboxTipoBusqueda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(fechaInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(fechaFin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscar)
                    .addComponent(btnLimpiar)
                    .addComponent(jButton2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
// Búsqueda por rango de fechas
    // Busca registros dentro de un rango de fechas seleccionado
    // Convierte fechas de JDateChooser a LocalDateTime
    public void buscarPorFechas() {
        if (fechaInicio.getDate() == null || fechaFin.getDate() == null) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Por favor seleccione ambas fechas",
            "Advertencia",
            javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

       // Convertir fechas a String
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
    String fInicio = sdf.format(fechaInicio.getDate());
    String fFin = sdf.format(fechaFin.getDate());

    BitacoraComisionesDAO bitacoraDAO = new BitacoraComisionesDAO();
    List<clsBitacoraComisionesVenta> bitacoras = bitacoraDAO.queryPorFechas(fInicio, fFin);

    DefaultTableModel modelo = (DefaultTableModel) tablaBitacoraComisiones.getModel();
    modelo.setRowCount(0);

    if (bitacoras.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "No se encontraron registros en ese rango de fechas",
            "Advertencia",
            javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }

    String[] dato = new String[7];
    for (int i = 0; i < bitacoras.size(); i++) {
        dato[0] = Integer.toString(bitacoras.get(i).getBitcodigo());
        dato[1] = Integer.toString(bitacoras.get(i).getUsucodigo());
        dato[2] = Integer.toString(bitacoras.get(i).getAplcodigo());
        dato[3] = bitacoras.get(i).getBitfecha();
        dato[4] = bitacoras.get(i).getBitip();
        dato[5] = bitacoras.get(i).getBitequipo();
        dato[6] = bitacoras.get(i).getBitaccion();
        modelo.addRow(dato);
    }
}

    // Limpiar
    // Limpia los campos de búsqueda
    // Oculta los controles de búsqueda
    // Recarga todos los registros en la tabla
    public void limpiar() {
        txtBuscar.setText("");
        cboxTipoBusqueda.setSelectedIndex(0);
        txtBuscar.setVisible(false);
        fechaInicio.setVisible(false);
        fechaFin.setVisible(false);
        btnBuscar.setVisible(false);
        llenadoDeTabla();
    }
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            if ((new File("src\\main\\java\\ayudas\\ayudaBitacora.chm")).exists()) {
                Process p = Runtime
                .getRuntime()
                .exec("rundll32 url.dll,FileProtocolHandler src\\main\\java\\ayudas\\ayudaBitacora.chm");
                p.waitFor();
            } else {
                System.out.println("La ayuda no Fue encontrada");
            }
            System.out.println("Correcto");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        // Obtiene el tipo de búsqueda seleccionado en el ComboBox
        String seleccion = cboxTipoBusqueda.getSelectedItem().toString();

        // Según la opción elegida se ejecuta el método de búsqueda correspondiente
        switch (seleccion) {
            case "Código":
            if (txtBuscar.getText().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Ingrese un código", "Advertencia",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            //busca por codigo
            buscarPorCodigo();
            break;
            case "Usuario":
            if (txtBuscar.getText().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Ingrese un código de usuario", "Advertencia",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            //busca por ususario
            buscarPorUsuario();
            break;
            case "Aplicación":
            if (txtBuscar.getText().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Ingrese un código de aplicación", "Advertencia",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            //busca por aplicacion
            buscarPorAplicacion();
            break;
            case "Accion":
            if (txtBuscar.getText().isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "Ingrese una acción", "Advertencia",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            //busca por accion
            buscarPorAccion();
            break;
            case "Rango de Fechas":
            buscarPorFechas();
            break;

            // En caso de no seleccionar una opción válida
            default:
            javax.swing.JOptionPane.showMessageDialog(this,
                "Seleccione un tipo de búsqueda", "Advertencia",
                javax.swing.JOptionPane.WARNING_MESSAGE);
        }
        //Agruegué el Registro de la accion en bitacora - Astrid
        BitacoraComisionesDAO bitacoraDAO = new BitacoraComisionesDAO();
        bitacoraDAO.insert(idUsuario, Aplcodigo, "CONSULTA");
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void cboxTipoBusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboxTipoBusquedaActionPerformed
        // TODO add your handling code here:

        // Oculta todos los campos de búsqueda al inicio
        // para evitar que aparezcan controles innecesarios
        txtBuscar.setVisible(false);
        fechaInicio.setVisible(false);
        fechaFin.setVisible(false);
        jLabel1.setVisible(false);
        jLabel2.setVisible(false);
        btnBuscar.setVisible(false);

        // Obtiene el tipo de búsqueda seleccionado en el ComboBox
        String seleccion = cboxTipoBusqueda.getSelectedItem().toString();

        // Dependiendo del tipo de búsqueda se muestran
        // los controles necesarios para ingresar los datos
        switch (seleccion) {
            // Para estas búsquedas se necesita un valor en el campo de texto
            case "Código":
            case "Usuario":
            case "Aplicación":
            case "Accion":
            txtBuscar.setVisible(true);
            btnBuscar.setVisible(true);
            break;
            // Para esta búsqueda se requiere seleccionar un rango de fechas
            case "Rango de Fechas":
            fechaInicio.setVisible(true);
            fechaFin.setVisible(true);
            jLabel1.setVisible(true);
            jLabel2.setVisible(true);
            btnBuscar.setVisible(true);
            break;
        }

    }//GEN-LAST:event_cboxTipoBusquedaActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<String> cboxTipoBusqueda;
    private com.toedter.calendar.JDateChooser fechaFin;
    private com.toedter.calendar.JDateChooser fechaInicio;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablaBitacoraComisiones;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
