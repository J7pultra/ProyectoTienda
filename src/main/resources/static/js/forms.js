/* ========================================
   FORMULARIOS ESPECÍFICOS
   ======================================== */

class FormManager {
    constructor() {
        this.initializeForms();
    }
    
    initializeForms() {
        this.initializeVentaForm();
        this.initializeClienteForm();
        this.initializeUniformeForm();
        this.initializeCalculators();
    }
    
    // Formulario de ventas
    initializeVentaForm() {
        const ventaForm = document.getElementById('ventaForm');
        if (!ventaForm) return;
        
        // Agregar producto a la venta
        const addProductBtn = document.getElementById('addProduct');
        if (addProductBtn) {
            addProductBtn.addEventListener('click', () => this.addProductToSale());
        }
        
        // Calcular totales automáticamente
        ventaForm.addEventListener('input', (e) => {
            if (e.target.matches('.cantidad, .precio')) {
                this.calculateSaleTotal();
            }
        });
    }
    
    addProductToSale() {
        const container = document.getElementById('productosContainer');
        if (!container) return;
        
        const productRow = document.createElement('div');
        productRow.className = 'row mb-3 product-row';
        productRow.innerHTML = `
            <div class="col-md-4">
                <select class="form-select producto-select" name="productos[].id" required>
                    <option value="">Seleccionar producto...</option>
                </select>
            </div>
            <div class="col-md-2">
                <input type="number" class="form-control cantidad" name="productos[].cantidad" 
                       placeholder="Cantidad" min="1" required>
            </div>
            <div class="col-md-2">
                <input type="number" class="form-control precio" name="productos[].precio" 
                       placeholder="Precio" step="0.01" readonly>
            </div>
            <div class="col-md-2">
                <input type="number" class="form-control subtotal" 
                       placeholder="Subtotal" readonly>
            </div>
            <div class="col-md-2">
                <button type="button" class="btn btn-danger btn-sm remove-product">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        `;
        
        container.appendChild(productRow);
        
        // Evento para eliminar producto
        productRow.querySelector('.remove-product').addEventListener('click', () => {
            productRow.remove();
            this.calculateSaleTotal();
        });
        
        // Cargar productos en el select
        this.loadProductsInSelect(productRow.querySelector('.producto-select'));
    }
    
    loadProductsInSelect(selectElement) {
        // Simular carga de productos
        fetch('/api/uniformes/disponibles')
            .then(response => response.json())
            .then(productos => {
                productos.forEach(producto => {
                    const option = document.createElement('option');
                    option.value = producto.id;
                    option.textContent = `${producto.nombre} - $${producto.precio}`;
                    option.dataset.precio = producto.precio;
                    selectElement.appendChild(option);
                });
            })
            .catch(error => {
                console.error('Error cargando productos:', error);
            });
        
        // Evento para actualizar precio cuando se selecciona producto
        selectElement.addEventListener('change', (e) => {
            const selectedOption = e.target.selectedOptions[0];
            const precioInput = e.target.closest('.product-row').querySelector('.precio');
            
            if (selectedOption && selectedOption.dataset.precio) {
                precioInput.value = selectedOption.dataset.precio;
                this.calculateSaleTotal();
            }
        });
    }
    
    calculateSaleTotal() {
        let subtotal = 0;
        
        document.querySelectorAll('.product-row').forEach(row => {
            const cantidad = parseFloat(row.querySelector('.cantidad').value) || 0;
            const precio = parseFloat(row.querySelector('.precio').value) || 0;
            const subtotalProducto = cantidad * precio;
            
            row.querySelector('.subtotal').value = subtotalProducto.toFixed(2);
            subtotal += subtotalProducto;
        });
        
        const descuento = parseFloat(document.getElementById('descuento')?.value) || 0;
        const impuesto = 0.19; // 19% IVA
        
        const descuentoAmount = subtotal * (descuento / 100);
        const baseImponible = subtotal - descuentoAmount;
        const impuestoAmount = baseImponible * impuesto;
        const total = baseImponible + impuestoAmount;
        
        // Actualizar campos
        document.getElementById('subtotalVenta').value = subtotal.toFixed(2);
        document.getElementById('descuentoAmount').value = descuentoAmount.toFixed(2);
        document.getElementById('impuestoAmount').value = impuestoAmount.toFixed(2);
        document.getElementById('totalVenta').value = total.toFixed(2);
    }
    
    // Formulario de clientes
    initializeClienteForm() {
        const clienteForm = document.getElementById('clienteForm');
        if (!clienteForm) return;
        
        // Validación de documento
        const documentoInput = clienteForm.querySelector('#documento');
        if (documentoInput) {
            documentoInput.addEventListener('blur', () => {
                this.validateDocumento(documentoInput.value);
            });
        }
        
        // Autocompletar ciudad basado en código postal
        const codigoPostalInput = clienteForm.querySelector('#codigoPostal');
        if (codigoPostalInput) {
            codigoPostalInput.addEventListener('input', debounce((e) => {
                this.autocompleteCiudad(e.target.value);
            }, 500));
        }
    }
    
    validateDocumento(documento) {
        if (!documento) return;
        
        fetch(`/api/clientes/validate-documento?documento=${documento}`)
            .then(response => response.json())
            .then(data => {
                const input = document.getElementById('documento');
                if (data.exists) {
                    input.classList.add('is-invalid');
                    showNotification('Ya existe un cliente con este documento', 'warning');
                } else {
                    input.classList.remove('is-invalid');
                    input.classList.add('is-valid');
                }
            })
            .catch(error => {
                console.error('Error validando documento:', error);
            });
    }
    
    // Formulario de uniformes
    initializeUniformeForm() {
        const uniformeForm = document.getElementById('uniformeForm');
        if (!uniformeForm) return;
        
        // Generar código automático
        const generarCodigoBtn = document.getElementById('generarCodigo');
        if (generarCodigoBtn) {
            generarCodigoBtn.addEventListener('click', () => {
                this.generateProductCode();
            });
        }
        
        // Calcular precio sugerido
        const costoInput = uniformeForm.querySelector('#costo');
        const margenInput = uniformeForm.querySelector('#margen');
        
        if (costoInput && margenInput) {
            [costoInput, margenInput].forEach(input => {
                input.addEventListener('input', () => {
                    this.calculateSuggestedPrice();
                });
            });
        }
    }
    
    generateProductCode() {
        const categoria = document.getElementById('categoria')?.value;
        const talla = document.getElementById('talla')?.value;
        const color = document.getElementById('color')?.value;
        
        if (categoria && talla && color) {
            const codigo = `${categoria.substring(0, 3).toUpperCase()}-${talla}-${color.substring(0, 3).toUpperCase()}-${Date.now().toString().slice(-4)}`;
            document.getElementById('codigo').value = codigo;
        } else {
            showNotification('Complete categoría, talla y color primero', 'warning');
        }
    }
    
    calculateSuggestedPrice() {
        const costo = parseFloat(document.getElementById('costo')?.value) || 0;
        const margen = parseFloat(document.getElementById('margen')?.value) || 0;
        
        if (costo > 0 && margen > 0) {
            const precioSugerido = costo * (1 + margen / 100);
            document.getElementById('precioSugerido').value = precioSugerido.toFixed(2);
        }
    }
    
    // Calculadoras
    initializeCalculators() {
        // Calculadora de descuentos
        document.querySelectorAll('.discount-calculator').forEach(calc => {
            calc.addEventListener('input', (e) => {
                this.calculateDiscount(e.target.closest('.discount-calculator'));
            });
        });
        
        // Calculadora de impuestos
        document.querySelectorAll('.tax-calculator').forEach(calc => {
            calc.addEventListener('input', (e) => {
                this.calculateTax(e.target.closest('.tax-calculator'));
            });
        });
    }
    
    calculateDiscount(container) {
        const precio = parseFloat(container.querySelector('.precio-original')?.value) || 0;
        const descuento = parseFloat(container.querySelector('.descuento-porcentaje')?.value) || 0;
        
        const descuentoAmount = precio * (descuento / 100);
        const precioFinal = precio - descuentoAmount;
        
        container.querySelector('.descuento-amount').value = descuentoAmount.toFixed(2);
        container.querySelector('.precio-final').value = precioFinal.toFixed(2);
    }
    
    calculateTax(container) {
        const subtotal = parseFloat(container.querySelector('.subtotal')?.value) || 0;
        const impuestoPorcentaje = parseFloat(container.querySelector('.impuesto-porcentaje')?.value) || 19;
        
        const impuestoAmount = subtotal * (impuestoPorcentaje / 100);
        const total = subtotal + impuestoAmount;
        
        container.querySelector('.impuesto-amount').value = impuestoAmount.toFixed(2);
        container.querySelector('.total').value = total.toFixed(2);
    }
}

// Inicializar cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    window.FormManager = new FormManager();
    
    
    
});
