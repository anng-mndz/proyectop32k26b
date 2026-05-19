/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Vista;


import java.util.List;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;


// Librerías de la Capa Controlador y Modelo
import Controlador.clsAsignacionPerfilUsuario;
import Modelo.AsignacionPerfilUsuarioDAO;
import Controlador.clsUsuario;
import Modelo.UsuarioDAO;
import Modelo.BitacoraDAO;
import Controlador.clsUsuarioConectado;
import Controlador.clsPerfil;
import Modelo.PerfilDAO;
import Modelo.PermisosDAO;
import Controlador.clsBitacora; 



/**
 *
 * @author JENNIFER BARRIOS 9959-24-10016 Modificacion:Roli Cedillo 9959-24-1672
 * 
 * ACTUALIZACIÓN: JENNIFER BARRIOS 
 */

public class frmProcesoPerfilUsuario extends javax.swing.JInternalFrame {

    private static final int Aplcodigo = 10010;
    private int idUsuarioConectado = clsUsuarioConectado.getUsuId();
    private boolean isSincronizando = false; 

    AsignacionPerfilUsuarioDAO asignacionDAO = new AsignacionPerfilUsuarioDAO();
    BitacoraDAO bitacoraDAO = new BitacoraDAO();
    PermisosDAO permisosDAO = new PermisosDAO();

    public frmProcesoPerfilUsuario() {
        initComponents();
        
        // CORRECCIÓN LOCAL: Si es Administrador (ID 1) entra directo. Si no, debe tener al menos permiso de Buscar para VER la información.
        if (idUsuarioConectado != 1 && !permisosDAO.puedeBuscar(idUsuarioConectado, Aplcodigo)) {
            JOptionPane.showMessageDialog(null, "No tiene acceso a este módulo para visualizar datos.");
            this.dispose();
            return;
        }

        this.setClosable(true);
        this.setIconifiable(true);
        this.setMaximizable(true);
        this.setResizable(true);

        cargarPermisos();
        llenarComboUsuario();

        if (cboUsuarioId.getItemCount() > 0) {
            cboUsuarioId.setSelectedIndex(0);
            int idInicial = Integer.parseInt(cboUsuarioId.getSelectedItem().toString());
            llenarTablas(idInicial);
        }
    }

    public void cargarPermisos() {
        // Usa los métodos internos adaptados para habilitar/deshabilitar los controles de la interfaz gráfica
        boolean flagInsertar = puedeInsertar();
        boolean flagEliminar = puedeEliminar();

        btnAsignarUno.setEnabled(flagInsertar);
        btnAsignarTodos.setEnabled(flagInsertar);
        btnQuitarUno.setEnabled(flagEliminar);
        btnQuitarTodos.setEnabled(flagEliminar);
    }

    // Métodos de validación locales con Bypass para el Administrador (ID 1) sin alterar el DAO original
    private boolean puedeInsertar() {
        if (idUsuarioConectado == 1) {
            return true; 
        }
        return permisosDAO.puedeInsertar(idUsuarioConectado, Aplcodigo);
    }

    private boolean puedeEliminar() {
        if (idUsuarioConectado == 1) {
            return true;
        }
        return permisosDAO.puedeEliminar(idUsuarioConectado, Aplcodigo);
    }

    public void llenarComboUsuario() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<clsUsuario> usuarios = usuarioDAO.consultaUsuarios();
        
        isSincronizando = true;
        cboUsuario.removeAllItems();
        cboUsuarioId.removeAllItems();

        for (clsUsuario usuario : usuarios) {
            cboUsuario.addItem(usuario.getUsuNombre());
            cboUsuarioId.addItem(String.valueOf(usuario.getUsuId()));
        }
        isSincronizando = false;
    }

    public void llenarTablas(int idUsuario) {
       DefaultTableModel modeloDisp = (DefaultTableModel) tablaDisponibles.getModel();
        DefaultTableModel modeloAsig = (DefaultTableModel) tablaAsignados.getModel();

        modeloDisp.setRowCount(0);
        modeloAsig.setRowCount(0);

        try {
            PerfilDAO daoPerfil = new PerfilDAO();
            
            // --- CORRECCIÓN LOCAL PARA ASIGNAR EL USUARIO CORRECTO ---
            clsBitacora bitacoraActiva = new clsBitacora();
      
            bitacoraActiva.setUsucodigo(idUsuarioConectado); 
            
             bitacoraActiva.setAplcodigo(Aplcodigo);
            bitacoraActiva.setBitaccion("Consulta de Perfiles");
            
            List<clsPerfil> perfiles = daoPerfil.obtenerPerfiles(bitacoraActiva); 

            for (clsPerfil perfil : perfiles) {
                if (asignacionDAO.buscar(idUsuario, perfil.getPercodigo())) {
                    modeloAsig.addRow(new Object[]{perfil.getPercodigo(), perfil.getPernombre()});
                } else {
                    if (perfil.getPerestado().equals("1") || perfil.getPerestado().equalsIgnoreCase("Activo")) {
                        modeloDisp.addRow(new Object[]{perfil.getPercodigo(), perfil.getPernombre(), perfil.getPerestado()});
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al llenar tablas: " + e.getMessage());
        }
    }
    
    /**
     * Creates new form MantenimientoAsignacionPerfilUsuario
     */
   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cboUsuario = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cboUsuarioId = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaDisponibles = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaAsignados = new javax.swing.JTable();
        btnAsignarUno = new javax.swing.JButton();
        btnQuitarUno = new javax.swing.JButton();
        btnAsignarTodos = new javax.swing.JButton();
        btnQuitarTodos = new javax.swing.JButton();
        btnayuda = new javax.swing.JButton();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("MANTENIMIENTO ASIGNACIÓN PERFIL USUARIO.");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 23, -1, -1));

        jLabel2.setText("Usuario");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 62, -1, -1));

        cboUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboUsuarioActionPerformed(evt);
            }
        });
        getContentPane().add(cboUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(69, 57, 154, -1));

        jLabel3.setText("Codigo Usuario: ");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(458, 62, -1, -1));

        cboUsuarioId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboUsuarioId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboUsuarioIdActionPerformed(evt);
            }
        });
        getContentPane().add(cboUsuarioId, new org.netbeans.lib.awtextra.AbsoluteConstraints(552, 57, 153, -1));

        jLabel4.setText("PERFILES DISPONIBLES:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 123, -1, -1));

        tablaDisponibles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Percodigo", "PerNombre", "PerEstado"
            }
        ));
        jScrollPane1.setViewportView(tablaDisponibles);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 151, 360, -1));

        jLabel5.setText("PERFILES ");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(458, 123, -1, -1));

        tablaAsignados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Percodigo", "PerNombre"
            }
        ));
        jScrollPane2.setViewportView(tablaAsignados);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(458, 151, 343, -1));

        btnAsignarUno.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAsignarUno.setText(">");
        btnAsignarUno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarUnoActionPerformed(evt);
            }
        });
        getContentPane().add(btnAsignarUno, new org.netbeans.lib.awtextra.AbsoluteConstraints(397, 232, -1, -1));

        btnQuitarUno.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnQuitarUno.setText("<");
        btnQuitarUno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarUnoActionPerformed(evt);
            }
        });
        getContentPane().add(btnQuitarUno, new org.netbeans.lib.awtextra.AbsoluteConstraints(401, 350, -1, -1));

        btnAsignarTodos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAsignarTodos.setText(">>");
        btnAsignarTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarTodosActionPerformed(evt);
            }
        });
        getContentPane().add(btnAsignarTodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 286, -1, -1));

        btnQuitarTodos.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnQuitarTodos.setText("<<");
        btnQuitarTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarTodosActionPerformed(evt);
            }
        });
        getContentPane().add(btnQuitarTodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(376, 412, -1, -1));

        btnayuda.setText("AYUDA");
        btnayuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnayudaActionPerformed(evt);
            }
        });
        getContentPane().add(btnayuda, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnQuitarUnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarUnoActionPerformed
    if (!puedeEliminar()) {
            JOptionPane.showMessageDialog(null, "No tiene permisos para quitar.");
            return;
        }
        int fila = tablaAsignados.getSelectedRow();
        if (fila != -1) {
            int idPerfil = Integer.parseInt(tablaAsignados.getValueAt(fila, 0).toString());
            int idUsuarioDestino = Integer.parseInt(cboUsuarioId.getSelectedItem().toString());
            if (asignacionDAO.eliminar(idUsuarioDestino, idPerfil) > 0) {
                bitacoraDAO.insert(idUsuarioConectado, Aplcodigo, "QUITÓ PERFIL: " + idPerfil);
                llenarTablas(idUsuarioDestino);
            }
        }
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btnQuitarUnoActionPerformed

    private void btnQuitarTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarTodosActionPerformed
   if (!puedeEliminar()) return;
        int idUsuarioDestino = Integer.parseInt(cboUsuarioId.getSelectedItem().toString());
        DefaultTableModel modelo = (DefaultTableModel) tablaAsignados.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            int idPerfil = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            asignacionDAO.eliminar(idUsuarioDestino, idPerfil);
        }
        bitacoraDAO.insert(idUsuarioConectado, Aplcodigo, "QUITÓ TODOS LOS PERFILES");
        llenarTablas(idUsuarioDestino);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btnQuitarTodosActionPerformed

    private void btnAsignarTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarTodosActionPerformed
   if (!puedeInsertar()) return;
        int idUsuarioDestino = Integer.parseInt(cboUsuarioId.getSelectedItem().toString());
        DefaultTableModel modelo = (DefaultTableModel) tablaDisponibles.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            int idPerfil = Integer.parseInt(modelo.getValueAt(i, 0).toString());
            asignacionDAO.insertar(idUsuarioDestino, idPerfil);
        }
        bitacoraDAO.insert(idUsuarioConectado, Aplcodigo, "ASIGNÓ TODOS LOS PERFILES");
        llenarTablas(idUsuarioDestino);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAsignarTodosActionPerformed

    private void btnAsignarUnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarUnoActionPerformed

    if (!puedeInsertar()) {
            JOptionPane.showMessageDialog(null, "No tiene permisos para asignar.");
            return;
        }
        int fila = tablaDisponibles.getSelectedRow();
        if (fila != -1) {
            int idPerfil = Integer.parseInt(tablaDisponibles.getValueAt(fila, 0).toString());
            int idUsuarioDestino = Integer.parseInt(cboUsuarioId.getSelectedItem().toString());
            if (asignacionDAO.insertar(idUsuarioDestino, idPerfil) > 0) {
                bitacoraDAO.insert(idUsuarioConectado, Aplcodigo, "ASIGNÓ PERFIL: " + idPerfil);
                llenarTablas(idUsuarioDestino);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un perfil.");
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_btnAsignarUnoActionPerformed

    private void cboUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboUsuarioActionPerformed
                                      
        if (!isSincronizando && cboUsuario.getSelectedIndex() != -1) {
            isSincronizando = true;
            cboUsuarioId.setSelectedIndex(cboUsuario.getSelectedIndex());
            llenarTablas(Integer.parseInt(cboUsuarioId.getSelectedItem().toString()));
            isSincronizando = false;
        }
      
 
        // TODO add your handling code here:
    }//GEN-LAST:event_cboUsuarioActionPerformed

    private void cboUsuarioIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboUsuarioIdActionPerformed
      if (!isSincronizando && cboUsuarioId.getSelectedIndex() != -1) {
            isSincronizando = true;
            cboUsuario.setSelectedIndex(cboUsuarioId.getSelectedIndex());
            llenarTablas(Integer.parseInt(cboUsuarioId.getSelectedItem().toString()));
            isSincronizando = false;
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_cboUsuarioIdActionPerformed

    private void btnayudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnayudaActionPerformed

        //AUTORA DEL CODIGO MARIA CELESTE MAYEN IBARRA. 
        //POR PROBLEMAS TÉCNICOS JENNIFER BARRIOS HA INSERTADO EL CODIGO DEL BOTON.
        //Codigo modificado. 
        
        String mensaje = "Instrucciones de Asignación:\n\n"
            + "1. Seleccione el usuario en el menú desplegable.\n"
            + "2. Para asignar: Seleccione un perfil de la izquierda y presione '>'.\n"
            + "3. Para quitar: Seleccione un perfil de la derecha y presione '<'.\n"
            + "4. Los botones '>>' y '<<' mueven todos los registros a la vez.";
    JOptionPane.showMessageDialog(this, mensaje, "Ayuda del Sistema", JOptionPane.INFORMATION_MESSAGE);
        
        // TODO add your handling code here:
    }//GEN-LAST:event_btnayudaActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmProcesoPerfilUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmProcesoPerfilUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmProcesoPerfilUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmProcesoPerfilUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmProcesoPerfilUsuario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignarTodos;
    private javax.swing.JButton btnAsignarUno;
    private javax.swing.JButton btnQuitarTodos;
    private javax.swing.JButton btnQuitarUno;
    private javax.swing.JButton btnayuda;
    private javax.swing.JComboBox<String> cboUsuario;
    private javax.swing.JComboBox<String> cboUsuarioId;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tablaAsignados;
    private javax.swing.JTable tablaDisponibles;
    // End of variables declaration//GEN-END:variables
}
