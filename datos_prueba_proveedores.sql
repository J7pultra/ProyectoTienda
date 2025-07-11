-- Script para insertar proveedores de prueba en la base de datos
-- Ejecutar después de asegurar que las tablas estén creadas

INSERT INTO proveedores (nombre, nit, email, telefono, direccion, ciudad, contacto, fecha_registro, activo, created_at, updated_at) VALUES
('Textiles San José S.A.', '123456789', 'ventas@textiles-sj.com', '+506-2234-5678', 'Av. Central 123', 'San José', 'Carlos Rodríguez', NOW(), true, NOW(), NOW()),
('Confecciones Modernas Ltda.', '987654321', 'pedidos@confeccionesmodernas.cr', '+506-2456-7890', 'Calle 5, Ave 10', 'Cartago', 'María González', NOW(), true, NOW(), NOW()),
('Uniformes Profesionales CR', '456789123', 'info@uniformesprofesionales.co.cr', '+506-2678-9012', 'Zona Industrial', 'Heredia', 'José Martínez', NOW(), true, NOW(), NOW()),
('Distribuidora La Universal', '789123456', 'compras@launiversal.cr', '+506-2890-1234', 'Mercado Central', 'San José', 'Ana López', NOW(), true, NOW(), NOW()),
('Textiles del Pacífico', '321654987', 'ventas@textilespacifico.com', '+506-2345-6789', 'Puerto de Puntarenas', 'Puntarenas', 'Roberto Jiménez', NOW(), true, NOW(), NOW())
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    email = VALUES(email),
    telefono = VALUES(telefono),
    direccion = VALUES(direccion),
    ciudad = VALUES(ciudad),
    contacto = VALUES(contacto),
    updated_at = NOW();

-- Verificar los proveedores insertados
SELECT id, nombre, nit, email, telefono, activo FROM proveedores WHERE activo = true;
