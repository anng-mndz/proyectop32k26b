package Vista;

/**
 * Vista para la Asignación de Aplicaciones a Usuarios
 * 
 * Permite asignar aplicaciones a usuarios del sistema, gestionar permisos
 * de acceso (Insert, Select, Update, Delete, Report) y mantener un registro
 * de todas las operaciones realizadas en la bitácora del sistema.
 * 
 * Funcionalidades:
 * - Seleccionar usuario y cargar sus aplicaciones
 * - Asignar una o múltiples aplicaciones a usuarios
 * - Modificar permisos de acceso a aplicaciones
 * - Eliminar asignaciones de aplicaciones
 * - Visualizar historial completo en la bitácora
 * - Registro automático de todas las acciones
 * 
 * @author Angel Méndez
 * @carnet 9959-24-6845
 * @since 2026-04-08
 */

import Controlador.clsAplicaciones;
import Controlador.clsAsignacionAplicacionUsuario;
import Controlador.clsBitacora;
import Controlador.clsUsuario;
import Modelo.BitacoraDAO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;

public class frmProcesoAplicacionUsuario extends javax.swing.JInternalFrame {

    // ── Modelos y listas ──────────────────────────────────────────────────
    private final DefaultListModel<String> modeloDisponibles = new DefaultListModel<>();
    private final DefaultListModel<String> modeloAsignadas   = new DefaultListModel<>();
    private List<clsAplicaciones> listaDisp     = new ArrayList<>();
    private List<clsAplicaciones> listaAsig     = new ArrayList<>();
    private List<clsUsuario>      listaUsuarios = new ArrayList<>();
    private DefaultTableModel     modeloBitacora;
    private int usuId;
    private int aplCodigo;

   /**
     * Obtiene el nombre de una aplicación por su código
     * (Busca en todas las listas disponibles)
     */
    private String obtenerNombreAplicacion(int aplCodigo) {
        // Primero busca en listaAsig
        if (listaAsig != null && !listaAsig.isEmpty()) {
            for (clsAplicaciones app : listaAsig) {
                if (app.getAplcodigo() == aplCodigo) {
                    return app.getAplnombre();
                }
            }
        }
        // Si no encuentra, busca en listaDisp
        if (listaDisp != null && !listaDisp.isEmpty()) {
            for (clsAplicaciones app : listaDisp) {
                if (app.getAplcodigo() == aplCodigo) {
                    return app.getAplnombre();
                }
            }
        }
        // Si aún no encuentra, retorna N/A (el fallback está en cargarBitacoraCompleta)
        return "N/A";
    }
    
    /**
     * Obtiene el nombre de un usuario por su ID
     */
    private String obtenerNombreUsuario(int usuId) {
        if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
            for (clsUsuario usu : listaUsuarios) {
                if (usu.getUsuId() == usuId) {
                    return usu.getUsuNombre();
                }
            }
        }
        return "N/A";
    }
    
    public frmProcesoAplicacionUsuario() {
        initComponents();
        configurarTabla();
        cargarUsuariosEnCombo();
        cargarBitacoraCompleta();
        habilitarSeleccionMultiple();  // ← ESTA LÍNEA PERMITE ASIGNAR Y ELIMINAR APLICACIONES A LA VEZ
        setVisible(true);              // ← La hace visible
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Asignacion Aplicaciones a Usuario");
        setDefaultCloseOperation(javax.swing.JInternalFrame.DISPOSE_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dlgPermisosAplicacion = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        chkInsert = new javax.swing.JCheckBox();
        chkSelect = new javax.swing.JCheckBox();
        chkUpdate = new javax.swing.JCheckBox();
        chkDelete = new javax.swing.JCheckBox();
        chkReport = new javax.swing.JCheckBox();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btnQuitar = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblBitacora = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbPerfil = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        btnCargar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstDisponibles = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstAsignadas = new javax.swing.JList<>();
        btnAsignar = new javax.swing.JButton();

        dlgPermisosAplicacion.setBackground(new java.awt.Color(0, 51, 102));
        dlgPermisosAplicacion.setFont(new java.awt.Font("Century Gothic", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(80, 80, 80));
        jLabel4.setText("Insert");

        jLabel5.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(80, 80, 80));
        jLabel5.setText("Select");

        jLabel6.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(80, 80, 80));
        jLabel6.setText("Delete");

        chkInsert.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        chkInsert.setText("S / N");

        chkSelect.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        chkSelect.setText("S / N");
        chkSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkSelectActionPerformed(evt);
            }
        });

        chkUpdate.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        chkUpdate.setText("S / N");

        chkDelete.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        chkDelete.setText("S / N");
        chkDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDeleteActionPerformed(evt);
            }
        });

        chkReport.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        chkReport.setText("S / N");

        btnGuardar.setBackground(new java.awt.Color(200, 200, 200));
        btnGuardar.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setBackground(new java.awt.Color(200, 200, 200));
        btnCancelar.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(80, 80, 80));
        jLabel9.setText("Update");

        jLabel10.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(80, 80, 80));
        jLabel10.setText("Report");

        javax.swing.GroupLayout dlgPermisosAplicacionLayout = new javax.swing.GroupLayout(dlgPermisosAplicacion.getContentPane());
        dlgPermisosAplicacion.getContentPane().setLayout(dlgPermisosAplicacionLayout);
        dlgPermisosAplicacionLayout.setHorizontalGroup(
            dlgPermisosAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgPermisosAplicacionLayout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addGroup(dlgPermisosAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dlgPermisosAplicacionLayout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                        .addComponent(btnCancelar))
                    .addGroup(dlgPermisosAplicacionLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkReport))
                    .addGroup(dlgPermisosAplicacionLayout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkDelete))
                    .addGroup(dlgPermisosAplicacionLayout.createSequentialGroup()
                        .addGroup(dlgPermisosAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(dlgPermisosAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkInsert, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(chkSelect, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(dlgPermisosAplicacionLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(chkUpdate)))
                .addGap(83, 83, 83))
        );
        dlgPermisosAplicacionLayout.setVerticalGroup(
            dlgPermisosAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dlgPermisosAplicacionLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(dlgPermisosAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(chkInsert))
                .addGap(18, 18, 18)
                .addGroup(dlgPermisosAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkSelect)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(dlgPermisosAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(chkUpdate))
                .addGap(18, 18, 18)
                .addGroup(dlgPermisosAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkDelete))
                .addGap(18, 18, 18)
                .addGroup(dlgPermisosAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(chkReport))
                .addGap(31, 31, 31)
                .addGroup(dlgPermisosAplicacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnQuitar.setBackground(new java.awt.Color(200, 200, 200));
        btnQuitar.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        btnQuitar.setText("<<");
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        tblBitacora.setBackground(new java.awt.Color(200, 200, 200));
        tblBitacora.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        tblBitacora.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Usuario", "Aplicacion", "Acción", "Fecha", "IP", "Equipo"
            }
        ));
        tblBitacora.setToolTipText("");
        tblBitacora.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane4.setViewportView(tblBitacora);

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel1.setText("Usuario");

        jLabel2.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(80, 80, 80));
        jLabel2.setText("Aplicaciones Disponibles");

        cmbPerfil.setBackground(new java.awt.Color(200, 200, 200));
        cmbPerfil.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        cmbPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(80, 80, 80));
        jLabel3.setText("Aplicaciones Asignadas");

        btnCargar.setBackground(new java.awt.Color(200, 200, 200));
        btnCargar.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        btnCargar.setText("Cargar");
        btnCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarActionPerformed(evt);
            }
        });

        lstDisponibles.setBackground(new java.awt.Color(200, 200, 200));
        lstDisponibles.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        lstDisponibles.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lstDisponibles);

        lstAsignadas.setBackground(new java.awt.Color(200, 200, 200));
        lstAsignadas.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        lstAsignadas.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        lstAsignadas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstAsignadasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(lstAsignadas);

        btnAsignar.setBackground(new java.awt.Color(200, 200, 200));
        btnAsignar.setFont(new java.awt.Font("Century Gothic", 1, 12)); // NOI18N
        btnAsignar.setText(">>");
        btnAsignar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAsignarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(139, 139, 139)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAsignar)
                                    .addComponent(btnQuitar))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 142, Short.MAX_VALUE)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnCargar)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(cmbPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3)))))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addComponent(btnCargar)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(82, 82, 82)
                                .addComponent(btnAsignar)
                                .addGap(41, 41, 41)
                                .addComponent(btnQuitar)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lstAsignadasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstAsignadasMouseClicked
        if (evt.getClickCount() == 2) {
            abrirPermisosDialog();
        }
    }//GEN-LAST:event_lstAsignadasMouseClicked

    private void btnAsignarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarActionPerformed
    asignarVariasAplicaciones();  // ← CAMBIO
    }//GEN-LAST:event_btnAsignarActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
    quitarVariasAplicaciones();  // ← CAMBIO
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void btnCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarActionPerformed
    cargarListas();
    }//GEN-LAST:event_btnCargarActionPerformed

    private void chkSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkSelectActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
 int idx = lstAsignadas.getSelectedIndex();
        if (idx < 0) return;
 
        clsUsuario usu = getUsuarioSeleccionado();
        if (usu == null) return;
 
        clsAplicaciones app = listaAsig.get(idx);
 
        String ins = chkInsert.isSelected() ? "1" : "0";
        String sel = chkSelect.isSelected() ? "1" : "0";
        String upd = chkUpdate.isSelected() ? "1" : "0";
        String del = chkDelete.isSelected() ? "1" : "0";
        String rep = chkReport.isSelected() ? "1" : "0";
 
        clsAsignacionAplicacionUsuario asig =
            new clsAsignacionAplicacionUsuario(
                app.getAplcodigo(),
                usu.getUsuId(),
                ins, sel, upd, del, rep
            );
 
        int res = new clsAsignacionAplicacionUsuario().setModificarPermisos(asig);
 
        if (res > 0) {
            cargarListas();
            cargarBitacoraCompleta();  // ← AGREGADO: RECARGAR BITÁCORA
            dlgPermisosAplicacion.dispose();
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void chkDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkDeleteActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
    dlgPermisosAplicacion.dispose();
    }//GEN-LAST:event_btnCancelarActionPerformed

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
            java.util.logging.Logger.getLogger(frmProcesoAplicacionUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmProcesoAplicacionUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmProcesoAplicacionUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmProcesoAplicacionUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmProcesoAplicacionUsuario().setVisible(true);
            }
        });
    }

private void configurarTabla() {
        modeloBitacora = new DefaultTableModel(
            new String[]{"Usuario", "Aplicación", "Acción", "Fecha", "IP", "Equipo"}, 0  // ← CAMBIO: Columnas actualizadas
        ) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblBitacora.setModel(modeloBitacora);
    }

private void cargarUsuariosEnCombo() {
    clsUsuario obj = new clsUsuario();
    listaUsuarios  = obj.getListadoUsuarios();

    DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
    modelo.addElement("-- Seleccione un usuario --");
    for (clsUsuario u : listaUsuarios) {
        modelo.addElement(u.getUsuId() + " - " + u.getUsuNombre());
    }
    cmbPerfil.setModel(modelo);
}

private clsUsuario getUsuarioSeleccionado() {
    int idx = cmbPerfil.getSelectedIndex();
    if (idx <= 0) return null;
    return listaUsuarios.get(idx - 1);
}

private void cargarListas() {
    clsUsuario usu = getUsuarioSeleccionado();
    if (usu == null) return;

    clsAsignacionAplicacionUsuario ctrl = new clsAsignacionAplicacionUsuario();
    listaDisp = ctrl.getAplicacionesDisponibles(usu.getUsuId());
    listaAsig = ctrl.getAplicacionesAsignadas(usu.getUsuId());

    modeloDisponibles.clear();
    for (clsAplicaciones a : listaDisp)
        modeloDisponibles.addElement(a.getAplcodigo() + " - " + a.getAplnombre());

    modeloAsignadas.clear();
    for (clsAplicaciones a : listaAsig)
        modeloAsignadas.addElement(a.getAplcodigo() + " - " + a.getAplnombre());

    lstDisponibles.setModel(modeloDisponibles);
    lstAsignadas.setModel(modeloAsignadas);
}

private void asignarAplicacion() {
        int idx = lstDisponibles.getSelectedIndex();
        if (idx < 0) return;

        clsUsuario usu = getUsuarioSeleccionado();
        if (usu == null) return;

        clsAplicaciones app = listaDisp.get(idx);

        clsAsignacionAplicacionUsuario asignacion =
            new clsAsignacionAplicacionUsuario(
                app.getAplcodigo(),
                usu.getUsuId(),
                "0","0","0","0","0"
            );

        int res = new clsAsignacionAplicacionUsuario().setAsignarAplicacion(asignacion);
        if (res > 0) {
            cargarListas();
            cargarBitacoraCompleta();  // ← AGREGADO: RECARGAR BITÁCORA
        }
    }

private void quitarAplicacion() {
        int idx = lstAsignadas.getSelectedIndex();
        if (idx < 0) return;

        clsUsuario usu = getUsuarioSeleccionado();
        if (usu == null) return;

        clsAplicaciones app = listaAsig.get(idx);
        clsAsignacionAplicacionUsuario asignacion = new clsAsignacionAplicacionUsuario();
        asignacion.setAplcodigo(app.getAplcodigo());
        asignacion.setUsuId(usu.getUsuId());

        int res = new clsAsignacionAplicacionUsuario().setQuitarAplicacion(asignacion);
        if (res > 0) {
            cargarListas();
            cargarBitacoraCompleta();  // ← AGREGADO: RECARGAR BITÁCORA
        }
    }

private void cargarBitacoraCompleta() { 
        try {
            modeloBitacora.setRowCount(0);

            BitacoraDAO dao = new BitacoraDAO();
            List<clsBitacora> listaBitacora = dao.select();

            if (listaBitacora != null && !listaBitacora.isEmpty()) {

                for (clsBitacora registro : listaBitacora) {

                    // ← MEJORADO: Obtener nombres de forma más robusta
                    String usuNombre = obtenerNombreUsuario(registro.getUsucodigo());
                    String aplNombre = obtenerNombreAplicacion(registro.getAplcodigo());
                    
                    // Si no encuentra el nombre, usa el código como fallback
                    if (usuNombre.equals("N/A")) {
                        usuNombre = "Usuario " + registro.getUsucodigo();
                    }
                    if (aplNombre.equals("N/A")) {
                        aplNombre = "App " + registro.getAplcodigo();
                    }

                    Object[] fila = {
                        usuNombre,
                        aplNombre,
                        registro.getBitaccion(),
                        registro.getBitfecha(),
                        registro.getBitip(),
                        registro.getBitequipo()
                    };

                    modeloBitacora.addRow(fila);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al cargar bitácora: " + e.getMessage());
            e.printStackTrace();
        }
    }

private void abrirPermisosDialog() {
    int idx = lstAsignadas.getSelectedIndex();
    if (idx < 0) return;
    clsUsuario usu = getUsuarioSeleccionado();
    if (usu == null) return;
    clsAplicaciones app = listaAsig.get(idx);
    int usuId = usu.getUsuId();
    int aplCodigo = app.getAplcodigo();
    clsAsignacionAplicacionUsuario ctrl =
            new clsAsignacionAplicacionUsuario();
    clsAsignacionAplicacionUsuario asig =
            ctrl.getPermisos(usuId, aplCodigo);
    if (asig != null) {
        chkInsert.setSelected("1".equals(asig.getAPLUins()));
        chkSelect.setSelected("1".equals(asig.getAPLUsel()));
        chkUpdate.setSelected("1".equals(asig.getAPLUupd()));
        chkDelete.setSelected("1".equals(asig.getAPLUdel()));
        chkReport.setSelected("1".equals(asig.getAPLUrep()));
    } else {
        chkInsert.setSelected(false);
        chkSelect.setSelected(false);
        chkUpdate.setSelected(false);
        chkDelete.setSelected(false);
        chkReport.setSelected(false);
    }
    dlgPermisosAplicacion.setSize(450, 350);  // ← DEFINIR TAMAÑO
    dlgPermisosAplicacion.setLocationRelativeTo(this);
    dlgPermisosAplicacion.setModal(true);
    dlgPermisosAplicacion.setVisible(true);
}

/**
     * NUEVO: Asignar VARIAS aplicaciones seleccionadas de una vez
     */
    private void asignarVariasAplicaciones() {
        int[] indices = lstDisponibles.getSelectedIndices();
        
        if (indices.length == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Selecciona al menos una aplicación", 
                "Advertencia", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        clsUsuario usu = getUsuarioSeleccionado();
        if (usu == null) return;

        int contador = 0;
        for (int idx : indices) {
            clsAplicaciones app = listaDisp.get(idx);

            clsAsignacionAplicacionUsuario asignacion =
                new clsAsignacionAplicacionUsuario(
                    app.getAplcodigo(),
                    usu.getUsuId(),
                    "0","0","0","0","0"
                );

            int res = new clsAsignacionAplicacionUsuario().setAsignarAplicacion(asignacion);
            if (res > 0) contador++;
        }
        
        if (contador > 0) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Se asignaron " + contador + " aplicación(es) correctamente", 
                "Éxito", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            cargarListas();
            cargarBitacoraCompleta();
        }
    }

    /**
     * NUEVO: Eliminar VARIAS aplicaciones seleccionadas de una vez
     */
    private void quitarVariasAplicaciones() {
        int[] indices = lstAsignadas.getSelectedIndices();
        
        if (indices.length == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Selecciona al menos una aplicación", 
                "Advertencia", 
                javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        clsUsuario usu = getUsuarioSeleccionado();
        if (usu == null) return;

        int contador = 0;
        for (int idx : indices) {
            clsAplicaciones app = listaAsig.get(idx);
            clsAsignacionAplicacionUsuario asignacion = new clsAsignacionAplicacionUsuario();
            asignacion.setAplcodigo(app.getAplcodigo());
            asignacion.setUsuId(usu.getUsuId());

            int res = new clsAsignacionAplicacionUsuario().setQuitarAplicacion(asignacion);
            if (res > 0) contador++;
        }
        
        if (contador > 0) {
            javax.swing.JOptionPane.showMessageDialog(this, 
                "Se eliminaron " + contador + " aplicación(es) correctamente", 
                "Éxito", 
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
            cargarListas();
            cargarBitacoraCompleta();
        }
    }

    /**
     * NUEVO: Habilitar selección múltiple en las listas
     */
    private void habilitarSeleccionMultiple() {
        lstDisponibles.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        lstAsignadas.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAsignar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCargar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JCheckBox chkDelete;
    private javax.swing.JCheckBox chkInsert;
    private javax.swing.JCheckBox chkReport;
    private javax.swing.JCheckBox chkSelect;
    private javax.swing.JCheckBox chkUpdate;
    private javax.swing.JComboBox<String> cmbPerfil;
    private javax.swing.JDialog dlgPermisosAplicacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList<String> lstAsignadas;
    private javax.swing.JList<String> lstDisponibles;
    private javax.swing.JTable tblBitacora;
    // End of variables declaration//GEN-END:variables
}
