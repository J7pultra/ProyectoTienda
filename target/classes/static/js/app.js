/* ========================================
   BOUTIQUE UNIFORMES - JAVASCRIPT PRINCIPAL
   ======================================== */

// Variables globales
let currentTheme = localStorage.getItem('theme') || 'light';
let sidebarCollapsed = localStorage.getItem('sidebarCollapsed') === 'true';
let currentUser = null;

// Configuración global
const CONFIG = {
    API_BASE_URL: '/api',
    NOTIFICATION_TIMEOUT: 5000,
    SEARCH_DELAY: 300,
    ANIMATION_DURATION: 300,
    DATE_FORMAT: 'DD/MM/YYYY',
    TIME_FORMAT: 'HH:mm',
    CURRENCY: 'COP'
};

// Inicialización cuando el DOM esté listo
$(document).ready(function() {
    initializeApp();
});

/* ========================================
   INICIALIZACIÓN DE LA APLICACIÓN
   ======================================== */

function initializeApp() {
    console.log('🚀 Inicializando Boutique Uniformes...');
    
    // Ocultar spinner de carga
    hideLoadingSpinner();
    
    // Inicializar componentes
    initializeTheme();
    initializeSidebar();
    initializeNotifications();
    initializeTooltips();
    initializeDataTables();
    initializeFormValidation();
    initializeSearchFunctionality();
    initializeTimeUpdates();
    initializeCharts();
    initializeModals();
    
    // Eventos globales
    bindGlobalEvents();
    
    console.log('✅ Aplicación inicializada correctamente');
}

/* ========================================
   GESTIÓN DE TEMA (CLARO/OSCURO)
   ======================================== */

function initializeTheme() {
    // Aplicar tema guardado
    if (currentTheme === 'dark') {
        document.body.setAttribute('data-bs-theme', 'dark');
        updateThemeIcons('dark');
    }
}

function toggleTheme() {
    const body = document.body;
    const newTheme = currentTheme === 'light' ? 'dark' : 'light';
    
    if (newTheme === 'dark') {
        body.setAttribute('data-bs-theme', 'dark');
    } else {
        body.removeAttribute('data-bs-theme');
    }
    
    currentTheme = newTheme;
    localStorage.setItem('theme', newTheme);
    updateThemeIcons(newTheme);
    
    // Notificación de cambio de tema
    showNotification(`Tema cambiado a ${newTheme === 'dark' ? 'oscuro' : 'claro'}`, 'info');
}

function updateThemeIcons(theme) {
    const themeIcons = document.querySelectorAll('#theme-icon, #theme-icon-menu');
    const themeTexts = document.querySelectorAll('#theme-text, #theme-text-menu');
    
    themeIcons.forEach(icon => {
        icon.className = theme === 'dark' ? 'fas fa-sun me-2' : 'fas fa-moon me-2';
    });
    
    themeTexts.forEach(text => {
        text.textContent = theme === 'dark' ? 'Modo Claro' : 'Modo Oscuro';
    });
}

/* ========================================
   GESTIÓN DEL SIDEBAR
   ======================================== */

function initializeSidebar() {
    // Restaurar estado del sidebar
    if (sidebarCollapsed) {
        collapseSidebar(false);
    }
    
    // Marcar enlace activo
    highlightActiveMenuItem();
}

function toggleSidebar() {
    sidebarCollapsed = !sidebarCollapsed;
    collapseSidebar(true);
    localStorage.setItem('sidebarCollapsed', sidebarCollapsed);
}

function collapseSidebar(animate = true) {
    const sidebar = document.querySelector('.sidebar');
    const mainContent = document.querySelector('.main-content');
    const toggleIcon = document.getElementById('sidebar-toggle-icon');
    
    if (!sidebar || !mainContent) return;
    
    if (sidebarCollapsed) {
        sidebar.classList.add('collapsed');
        mainContent.style.marginLeft = '80px';
        mainContent.style.width = 'calc(100% - 80px)';
        if (toggleIcon) toggleIcon.className = 'fas fa-angle-right';
    } else {
        sidebar.classList.remove('collapsed');
        mainContent.style.marginLeft = '260px';
        mainContent.style.width = 'calc(100% - 260px)';
        if (toggleIcon) toggleIcon.className = 'fas fa-angle-left';
    }
    
    if (animate) {
        sidebar.style.transition = 'all 0.3s ease';
        mainContent.style.transition = 'all 0.3s ease';
        
        setTimeout(() => {
            sidebar.style.transition = '';
            mainContent.style.transition = '';
        }, 300);
    }
}

function highlightActiveMenuItem() {
    const currentPath = window.location.pathname;
    const menuLinks = document.querySelectorAll('.menu-link, .sidebar-link');
    
    menuLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href && currentPath.startsWith(href) && href !== '/') {
            link.classList.add('active');
            
            // Expandir submenu si existe
            const submenu = link.nextElementSibling;
            if (submenu && submenu.classList.contains('submenu')) {
                submenu.style.display = 'block';
            }
        }
    });
}

/* ========================================
   SISTEMA DE NOTIFICACIONES
   ======================================== */

function initializeNotifications() {
    // Auto-hide alerts después de 5 segundos
    setTimeout(() => {
        $('.alert').fadeOut(500);
    }, CONFIG.NOTIFICATION_TIMEOUT);
    
    // Configurar SweetAlert2 por defecto
    if (typeof Swal !== 'undefined') {
        Swal.mixin({
            customClass: {
                confirmButton: 'btn btn-primary me-2',
                cancelButton: 'btn btn-secondary'
            },
            buttonsStyling: false
        });
    }
}

function showNotification(message, type = 'info', title = null) {
    if (typeof Swal !== 'undefined') {
        const icons = {
            success: 'success',
            error: 'error',
            warning: 'warning',
            info: 'info'
        };
        
        Swal.fire({
            icon: icons[type] || 'info',
            title: title || (type === 'error' ? 'Error' : 'Información'),
            text: message,
            timer: 3000,
            showConfirmButton: false,
            toast: true,
            position: 'top-end'
        });
    } else {
        // Fallback a alert nativo
        alert(message);
    }
}

function showConfirmDialog(message, callback, title = '¿Estás seguro?') {
    if (typeof Swal !== 'undefined') {
        Swal.fire({
            title: title,
            text: message,
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Sí, continuar',
            cancelButtonText: 'Cancelar',
            reverseButtons: true
        }).then((result) => {
            if (result.isConfirmed && callback) {
                callback();
            }
        });
    } else {
        if (confirm(message) && callback) {
            callback();
        }
    }
}

/* ========================================
   DATATABLES
   ======================================== */

function initializeDataTables() {
    if (typeof $.fn.DataTable !== 'undefined') {
        // Configuración por defecto para DataTables
        $.extend(true, $.fn.dataTable.defaults, {
            language: {
                url: '//cdn.datatables.net/plug-ins/1.13.7/i18n/es-ES.json'
            },
            responsive: true,
            pageLength: 10,
            lengthMenu: [[10, 25, 50, 100], [10, 25, 50, 100]],
            dom: '<"row"<"col-sm-12 col-md-6"l><"col-sm-12 col-md-6"f>>' +
                 '<"row"<"col-sm-12"tr>>' +
                 '<"row"<"col-sm-12 col-md-5"i><"col-sm-12 col-md-7"p>>',
            columnDefs: [{
                targets: 'no-sort',
                orderable: false
            }]
        });
        
        // Inicializar todas las tablas con clase 'data-table'
        $('.data-table').each(function() {
            if (!$.fn.DataTable.isDataTable(this)) {
                $(this).DataTable();
            }
        });
    }
}

/* ========================================
   VALIDACIÓN DE FORMULARIOS
   ======================================== */

function initializeFormValidation() {
    // Bootstrap validation
    const forms = document.querySelectorAll('.needs-validation');
    
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
                
                // Mostrar primer error
                const firstInvalid = form.querySelector(':invalid');
                if (firstInvalid) {
                    firstInvalid.focus();
                    showNotification('Por favor, completa todos los campos requeridos', 'warning');
                }
            }
            
            form.classList.add('was-validated');
        });
    });
    
    // Validación en tiempo real
    document.querySelectorAll('input, select, textarea').forEach(input => {
        input.addEventListener('blur', function() {
            validateField(this);
        });
        
        input.addEventListener('input', function() {
            if (this.classList.contains('is-invalid')) {
                validateField(this);
            }
        });
    });
}

function validateField(field) {
    const isValid = field.checkValidity();
    
    field.classList.remove('is-valid', 'is-invalid');
    field.classList.add(isValid ? 'is-valid' : 'is-invalid');
    
    return isValid;
}

function validateForm(formSelector) {
    const form = document.querySelector(formSelector);
    if (!form) return false;
    
    let isValid = true;
    const inputs = form.querySelectorAll('input, select, textarea');
    
    inputs.forEach(input => {
        if (!validateField(input)) {
            isValid = false;
        }
    });
    
    return isValid;
}

/* ========================================
   BÚSQUEDA GLOBAL
   ======================================== */

function initializeSearchFunctionality() {
    const searchInput = document.getElementById('globalSearch');
    if (searchInput) {
        let searchTimeout;
        
        searchInput.addEventListener('input', function() {
            clearTimeout(searchTimeout);
            const query = this.value.trim();
            
            if (query.length >= 2) {
                searchTimeout = setTimeout(() => {
                    performGlobalSearch(query);
                }, CONFIG.SEARCH_DELAY);
            }
        });
        
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                const query = this.value.trim();
                if (query) {
                    window.location.href = `/buscar?q=${encodeURIComponent(query)}`;
                }
            }
        });
    }
}

function performGlobalSearch(query) {
    // Implementar búsqueda AJAX aquí
    console.log('Buscando:', query);
    
    // Ejemplo de implementación
    /*
    $.ajax({
        url: '/api/search',
        method: 'GET',
        data: { q: query },
        success: function(results) {
            displaySearchResults(results);
        },
        error: function() {
            showNotification('Error en la búsqueda', 'error');
        }
    });
    */
}

/* ========================================
   ACTUALIZACIÓN DE TIEMPO
   ======================================== */

function initializeTimeUpdates() {
    updateAllTimes();
    
    // Actualizar cada minuto
    setInterval(updateAllTimes, 60000);
}

function updateAllTimes() {
    const now = new Date();
    
    // Actualizar reloj en navbar
    const navbarTime = document.getElementById('current-time');
    if (navbarTime) {
        navbarTime.textContent = formatTime(now);
    }
    
    // Actualizar reloj en sidebar
    const sidebarTime = document.getElementById('sidebar-time');
    if (sidebarTime) {
        sidebarTime.textContent = formatTime(now);
    }
    
    // Actualizar timestamps relativos
    updateRelativeTimestamps();
}

function formatTime(date) {
    return date.toLocaleTimeString('es-ES', {
        hour: '2-digit',
        minute: '2-digit'
    });
}

function formatDate(date) {
    return date.toLocaleDateString('es-ES', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
}

function updateRelativeTimestamps() {
    document.querySelectorAll('[data-timestamp]').forEach(element => {
        const timestamp = element.getAttribute('data-timestamp');
        const date = new Date(timestamp);
        element.textContent = getRelativeTime(date);
    });
}

function getRelativeTime(date) {
    const now = new Date();
    const diff = now - date;
    const minutes = Math.floor(diff / 60000);
    const hours = Math.floor(minutes / 60);
    const days = Math.floor(hours / 24);
    
    if (minutes < 1) return 'Ahora';
    if (minutes < 60) return `Hace ${minutes} min`;
    if (hours < 24) return `Hace ${hours} h`;
    if (days < 7) return `Hace ${days} días`;
    
    return formatDate(date);
}

/* ========================================
   GRÁFICOS
   ======================================== */

function initializeCharts() {
    if (typeof Chart !== 'undefined') {
        // Configuración por defecto para Chart.js
        Chart.defaults.font.family = "'Inter', sans-serif";
        Chart.defaults.color = '#6b7280';
        
        // Inicializar gráficos específicos
        initializeDashboardCharts();
    }
}

function initializeDashboardCharts() {
    // Gráfico de ventas
    const salesChartCanvas = document.getElementById('salesChart');
    if (salesChartCanvas) {
        createSalesChart(salesChartCanvas);
    }
    
    // Gráfico de productos más vendidos
    const productsChartCanvas = document.getElementById('productsChart');
    if (productsChartCanvas) {
        createProductsChart(productsChartCanvas);
    }
}

function createSalesChart(canvas) {
    new Chart(canvas, {
        type: 'line',
        data: {
            labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
            datasets: [{
                label: 'Ventas',
                data: [12, 19, 3, 5, 2, 3],
                borderColor: '#4f46e5',
                backgroundColor: 'rgba(79, 70, 229, 0.1)',
                tension: 0.4,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: 'rgba(0, 0, 0, 0.05)'
                    }
                },
                x: {
                    grid: {
                        display: false
                    }
                }
            }
        }
    });
}

function createProductsChart(canvas) {
    new Chart(canvas, {
        type: 'doughnut',
        data: {
            labels: ['Camisas', 'Pantalones', 'Faldas', 'Zapatos'],
            datasets: [{
                data: [30, 25, 20, 25],
                backgroundColor: [
                    '#4f46e5',
                    '#10b981',
                    '#f59e0b',
                    '#ef4444'
                ],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}

/* ========================================
   MODALES
   ======================================== */

function initializeModals() {
    // Configurar modales de confirmación
    document.querySelectorAll('[data-confirm]').forEach(element => {
        element.addEventListener('click', function(e) {
            e.preventDefault();
            const message = this.getAttribute('data-confirm');
            const href = this.getAttribute('href') || this.getAttribute('data-href');
            
            showConfirmDialog(message, () => {
                if (href) {
                    window.location.href = href;
                } else if (this.tagName === 'FORM') {
                    this.submit();
                }
            });
        });
    });
}

/* ========================================
   TOOLTIPS Y POPOVERS
   ======================================== */

function initializeTooltips() {
    // Inicializar tooltips de Bootstrap
    if (typeof bootstrap !== 'undefined') {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(function(tooltipTriggerEl) {
            return new bootstrap.Tooltip(tooltipTriggerEl);
        });
        
        // Inicializar popovers
        const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
        popoverTriggerList.map(function(popoverTriggerEl) {
            return new bootstrap.Popover(popoverTriggerEl);
        });
    }
}

/* ========================================
   EVENTOS GLOBALES
   ======================================== */

function bindGlobalEvents() {
    // Botón volver arriba
    const backToTopBtn = document.querySelector('.back-to-top');
    if (backToTopBtn) {
        window.addEventListener('scroll', function() {
            if (window.pageYOffset > 300) {
                backToTopBtn.classList.add('show');
            } else {
                backToTopBtn.classList.remove('show');
            }
        });
    }
    
    // Cerrar dropdowns al hacer click fuera
    document.addEventListener('click', function(e) {
        if (!e.target.closest('.dropdown')) {
            document.querySelectorAll('.dropdown-menu.show').forEach(menu => {
                menu.classList.remove('show');
            });
        }
    });
    
    // Prevenir envío de formularios vacíos
    document.addEventListener('submit', function(e) {
        const form = e.target;
        if (form.classList.contains('prevent-empty')) {
            const inputs = form.querySelectorAll('input[required], select[required], textarea[required]');
            let hasEmpty = false;
            
            inputs.forEach(input => {
                if (!input.value.trim()) {
                    hasEmpty = true;
                }
            });
            
            if (hasEmpty) {
                e.preventDefault();
                showNotification('Por favor, completa todos los campos requeridos', 'warning');
            }
        }
    });
}

/* ========================================
   UTILIDADES
   ======================================== */

function hideLoadingSpinner() {
    const spinner = document.getElementById('loading-spinner');
    if (spinner) {
        setTimeout(() => {
            spinner.style.opacity = '0';
            setTimeout(() => {
                spinner.style.display = 'none';
            }, 300);
        }, 500);
    }
}

function showLoadingSpinner() {
    const spinner = document.getElementById('loading-spinner');
    if (spinner) {
        spinner.style.display = 'flex';
        spinner.style.opacity = '1';
    }
}

function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

function formatCurrency(amount) {
    return new Intl.NumberFormat('es-CO', {
        style: 'currency',
        currency: CONFIG.CURRENCY
    }).format(amount);
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    };
}

/* ========================================
   API HELPERS
   ======================================== */

function apiRequest(endpoint, options = {}) {
    const defaultOptions = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        }
    };
    
    const config = { ...defaultOptions, ...options };
    
    return fetch(CONFIG.API_BASE_URL + endpoint, config)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .catch(error => {
            console.error('API Error:', error);
            showNotification('Error en la comunicación con el servidor', 'error');
            throw error;
        });
}

/* ========================================
   FUNCIONES ESPECÍFICAS DEL NEGOCIO
   ======================================== */

function calculateTotal(subtotal, discount = 0, tax = 0.19) {
    const discountAmount = subtotal * (discount / 100);
    const taxableAmount = subtotal - discountAmount;
    const taxAmount = taxableAmount * tax;
    return taxableAmount + taxAmount;
}

function validateStock(productId, quantity) {
    // Implementar validación de stock
    return apiRequest(`/products/${productId}/stock`)
        .then(data => {
            return data.stock >= quantity;
        });
}

function updateNotificationBadge(count) {
    const badges = document.querySelectorAll('.notification-badge');
    badges.forEach(badge => {
        if (count > 0) {
            badge.textContent = count;
            badge.style.display = 'inline';
        } else {
            badge.style.display = 'none';
        }
    });
}

/* ========================================
   EXPORTAR FUNCIONES GLOBALES
   ======================================== */

// Hacer funciones disponibles globalmente
window.BoutiqueApp = {
    toggleTheme,
    toggleSidebar,
    showNotification,
    showConfirmDialog,
    validateForm,
    formatCurrency,
    scrollToTop,
    apiRequest,
    calculateTotal,
    validateStock,
    updateNotificationBadge
};

// Funciones específicas para formularios
window.FormUtils = {
    validateField,
    validateForm
};

// Funciones de utilidad
window.Utils = {
    debounce,
    throttle,
    formatDate,
    formatTime,
    getRelativeTime
};

console.log('📦 Boutique Uniformes JS cargado correctamente');
