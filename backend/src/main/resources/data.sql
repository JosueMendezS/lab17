-- ==============================================================================
-- Datos Iniciales de Prueba - InsecurePay Digital Banking Portal
-- Contrasenas almacenadas con hash BCrypt (work factor 10)
-- ==============================================================================

INSERT INTO USUARIOS (username, password, nombre_completo, email, rol) VALUES
('admin', '$2a$10$XVEqy3xvCkjKzwUyUxgVCOwz3SNCGyAuK79R/AdIEYgydBNMiQ1uO', 'Administrador General de Sistemas', 'admin@insecurepay.fi.cr', 'ADMIN'),
('auditor_seguridad', '$2a$10$PXGXUL1TkXakCLv9gCTm7epc5HB6Mr9iJCHYSL1BwibbtB5qnCZla', 'Lic. Esteban Solano Chaves', 'esolano@insecurepay.fi.cr', 'AUDITOR'),
('carlos_mora', '$2a$10$DlPVOO1qjhEOX7F7gifJAusuy4IAClc9/0bkhmgCDHqZ.gboVvE.2', 'Carlos Mora Morales', 'cmora@correo.cr', 'CLIENTE'),
('maria_rojas', '$2a$10$HFRal2TZ3HtahFcdQf0TP.DFK3dAe32QcYcjtMysGSmpXJFhUTbqO', 'María Rojas Soto', 'mrojas@empresa.cr', 'CLIENTE'),
('juan_perez', '$2a$10$CeMN3VF8pNup7Qf3TXWuduJK7ai4ZFvStcLN1hNpfKk7eQ3CLqs1S', 'Juan Pérez Delgado', 'jperez@correo.cr', 'CLIENTE');

INSERT INTO CUENTAS_BANCARIAS (usuario_id, numero_cuenta, tipo_cuenta, saldo, moneda, activa) VALUES
(1, 'CR05015202001026458900', 'CORRIENTE', 50000000.00, 'CRC', TRUE),
(3, 'CR05015202001026458901', 'AHORROS', 250000.00, 'CRC', TRUE),
(4, 'CR05015202001026458902', 'CORRIENTE', 4850000.00, 'CRC', TRUE),
(5, 'CR05015202001026458903', 'AHORROS', 75000.00, 'CRC', TRUE);

INSERT INTO TRANSACCIONES (cuenta_origen_id, cuenta_destino_id, monto, concepto_html, fecha, estado) VALUES
(2, 3, 45000.00, '<p>Pago de servicios profesionales de programacion. <span style="color: green; font-weight: bold;">Verificado</span></p>', '2026-03-01 10:15:00', 'COMPLETADA'),
(3, 4, 120000.00, '<p>Cancelacion cuota de alquiler oficina central. Factura electronica #8849.</p>', '2026-03-05 14:30:00', 'COMPLETADA'),
(3, 2, 15000.00, '<p>Reembolso almuerzo equipo de trabajo.</p>', '2026-03-12 11:20:00', 'COMPLETADA'),
(4, 2, 35000.00, '<p>Aporte fondo solidario cooperativo. <span style="color: blue;">Transferencia inmediata SINPE Movil</span></p>', '2026-03-20 09:45:00', 'COMPLETADA');

