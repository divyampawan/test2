// Ecommerce Store Frontend JavaScript

class EcommerceApp {
    constructor() {
        this.token = localStorage.getItem('token');
        this.currentUser = null;
        this.cart = [];
    }

    init() {
        console.log('Initializing EcommerceApp...');
        this.setupEventListeners();
        this.checkAuthStatus();
        console.log('EcommerceApp initialized successfully');
    }

    setupEventListeners() {
        console.log('Setting up event listeners...');
        
        // Login form
        const loginForm = document.getElementById('loginForm');
        if (loginForm) {
            loginForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.login();
            });
            console.log('Login form listener added');
        } else {
            console.log('Login form not found');
        }

        // Register form
        const registerForm = document.getElementById('registerForm');
        if (registerForm) {
            registerForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.register();
            });
            console.log('Register form listener added');
        } else {
            console.log('Register form not found');
        }

        // Profile form
        const profileForm = document.getElementById('profileForm');
        if (profileForm) {
            profileForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.updateProfile();
            });
            console.log('Profile form listener added');
        }

        // Password form
        const passwordForm = document.getElementById('passwordForm');
        if (passwordForm) {
            passwordForm.addEventListener('submit', (e) => {
                e.preventDefault();
                this.changePassword();
            });
            console.log('Password form listener added');
        }

        // Password confirmation validation
        const confirmPassword = document.getElementById('confirmPassword');
        const newPassword = document.getElementById('newPassword');
        
        if (confirmPassword && newPassword) {
            confirmPassword.addEventListener('input', function() {
                const newPasswordValue = newPassword.value;
                const confirmPasswordValue = this.value;
                
                if (newPasswordValue !== confirmPasswordValue) {
                    this.setCustomValidity('Passwords do not match');
                } else {
                    this.setCustomValidity('');
                }
            });
            
            newPassword.addEventListener('input', function() {
                const confirmPasswordValue = confirmPassword.value;
                if (confirmPasswordValue) {
                    if (this.value !== confirmPasswordValue) {
                        confirmPassword.setCustomValidity('Passwords do not match');
                    } else {
                        confirmPassword.setCustomValidity('');
                    }
                }
            });
        }
    }

    checkAuthStatus() {
        console.log('Checking auth status...');
        if (this.token) {
            console.log('Token found, showing authenticated UI');
            this.showAuthenticatedUI();
            this.loadUserProfile();
        } else {
            console.log('No token found, showing unauthenticated UI');
            this.showUnauthenticatedUI();
        }
    }

    showAuthenticatedUI() {
        console.log('Showing authenticated UI...');
        const authPage = document.getElementById('auth-page');
        const mainNav = document.getElementById('main-nav');
        const mainContent = document.getElementById('main-content');
        
        if (authPage) {
            authPage.style.display = 'none';
            console.log('Auth page hidden');
        } else {
            console.log('Auth page not found');
        }
        if (mainNav) {
            mainNav.style.display = 'block';
            console.log('Main nav shown');
        } else {
            console.log('Main nav not found');
        }
        if (mainContent) {
            mainContent.style.display = 'block';
            console.log('Main content shown, display style:', mainContent.style.display);
        } else {
            console.log('Main content not found');
        }
        
        // Ensure products page is visible and load products
        this.hideAllPagesExcept('products-page');
        const productsPage = document.getElementById('products-page');
        if (productsPage) {
            productsPage.style.display = 'block';
            console.log('Products page shown');
            this.loadProducts();
            this.updateActiveNav('products');
        } else {
            console.log('Products page not found');
        }
    }

    showUnauthenticatedUI() {
        console.log('Showing unauthenticated UI...');
        const authPage = document.getElementById('auth-page');
        const mainNav = document.getElementById('main-nav');
        const mainContent = document.getElementById('main-content');
        
        if (authPage) authPage.style.display = 'flex';
        if (mainNav) mainNav.style.display = 'none';
        if (mainContent) mainContent.style.display = 'none';
    }

    async login() {
        const username = document.getElementById('loginUsername').value;
        const password = document.getElementById('loginPassword').value;

        try {
            const response = await fetch('/api/v1/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, password })
            });

            if (response.ok) {
                const data = await response.json();
                this.token = data.token;
                localStorage.setItem('token', this.token);
                this.showAlert('auth-alert', 'Login successful!', 'success');
                this.checkAuthStatus();
            } else {
                const error = await response.text();
                this.showAlert('auth-alert', 'Login failed: ' + error, 'danger');
            }
        } catch (error) {
            this.showAlert('auth-alert', 'Login failed: ' + error.message, 'danger');
        }
    }

    async register() {
        const username = document.getElementById('registerUsername').value;
        const email = document.getElementById('registerEmail').value;
        const password = document.getElementById('registerPassword').value;

        try {
            const response = await fetch('/api/v1/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username, email, password })
            });

            if (response.ok) {
                const data = await response.json();
                this.token = data.token;
                localStorage.setItem('token', this.token);
                this.showAlert('auth-alert', 'Registration successful!', 'success');
                this.checkAuthStatus();
            } else {
                const error = await response.text();
                this.showAlert('auth-alert', 'Registration failed: ' + error, 'danger');
            }
        } catch (error) {
            this.showAlert('auth-alert', 'Registration failed: ' + error.message, 'danger');
        }
    }

    logout() {
        console.log('Logging out...');
        this.token = null;
        this.currentUser = null;
        this.cart = [];
        localStorage.removeItem('token');
        this.showUnauthenticatedUI();
    }

    async loadUserProfile() {
        try {
            const response = await this.authenticatedRequest('/api/v1/users/profile');
            if (response.ok) {
                const user = await response.json();
                this.currentUser = user;
                
                // Update navigation display
                const usernameDisplay = document.getElementById('username-display');
                if (usernameDisplay) usernameDisplay.textContent = user.username;
                
                // Update profile page
                const profileUsername = document.getElementById('profile-username');
                if (profileUsername) profileUsername.textContent = user.username;
                
                const infoUsername = document.getElementById('info-username');
                if (infoUsername) infoUsername.textContent = user.username;
                
                const infoEmail = document.getElementById('info-email');
                if (infoEmail) infoEmail.textContent = user.email;
                
                const infoPhone = document.getElementById('info-phone');
                if (infoPhone) infoPhone.textContent = user.phoneNumber || 'Not provided';
                
                const infoUserid = document.getElementById('info-userid');
                if (infoUserid) infoUserid.textContent = user.id;
                
                const memberId = document.getElementById('member-id');
                if (memberId) memberId.textContent = user.id;
                
                // Set member since date (using user ID as a simple example)
                const memberSince = document.getElementById('member-since');
                if (memberSince) {
                    const memberDate = new Date(2024, 0, 1); // Default to Jan 2024
                    memberSince.textContent = memberDate.toLocaleDateString('en-US', { month: 'short', year: 'numeric' });
                }
            }
        } catch (error) {
            console.error('Failed to load user profile:', error);
        }
    }

    async updateProfile() {
        const username = document.getElementById('profileUsername').value;
        const email = document.getElementById('profileEmail').value;

        try {
            const response = await this.authenticatedRequest('/api/v1/users/profile', {
                method: 'PUT',
                body: JSON.stringify({ username, email })
            });

            if (response.ok) {
                this.showAlert('profile-alert', 'Profile updated successfully!', 'success');
                this.loadUserProfile();
            } else {
                const error = await response.text();
                this.showAlert('profile-alert', 'Profile update failed: ' + error, 'danger');
            }
        } catch (error) {
            this.showAlert('profile-alert', 'Profile update failed: ' + error.message, 'danger');
        }
    }

    async changePassword() {
        const currentPassword = document.getElementById('currentPassword').value;
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        if (newPassword !== confirmPassword) {
            this.showAlert('password-alert', 'New passwords do not match', 'danger');
            return;
        }

        if (newPassword.length < 6) {
            this.showAlert('password-alert', 'Password must be at least 6 characters', 'danger');
            return;
        }

        try {
            const response = await this.authenticatedRequest('/api/v1/users/change-password', {
                method: 'POST',
                body: JSON.stringify({ currentPassword, newPassword })
            });

            if (response.ok) {
                this.showAlert('password-alert', 'Password changed successfully!', 'success');
                const passwordForm = document.getElementById('passwordForm');
                if (passwordForm) passwordForm.reset();
            } else {
                const error = await response.text();
                this.showAlert('password-alert', 'Password change failed: ' + error, 'danger');
            }
        } catch (error) {
            this.showAlert('password-alert', 'Password change failed: ' + error.message, 'danger');
        }
    }

    async loadProducts() {
        console.log('Loading products...');
        try {
            const response = await fetch('/api/v1/products');
            console.log('Products response status:', response.status);
            if (response.ok) {
                const products = await response.json();
                console.log('Products loaded:', products);
                this.displayProducts(products);
            } else {
                console.log('Failed to load products, status:', response.status);
                this.showAlert('products-alert', 'Failed to load products', 'danger');
            }
        } catch (error) {
            console.log('Error loading products:', error);
            this.showAlert('products-alert', 'Failed to load products: ' + error.message, 'danger');
        }
    }

    displayProducts(products) {
        console.log('Displaying products:', products);
        const container = document.getElementById('products-grid');
        
        if (!container) {
            console.log('Products grid container not found');
            return;
        }
        
        console.log('Found products grid container, clearing...');
        container.innerHTML = '';

        if (products.length === 0) {
            console.log('No products to display');
            container.innerHTML = `
                <div class="col-12">
                    <div class="text-center">
                        <i class="fas fa-box-open" style="font-size: 4rem; color: #ccc; margin-bottom: 20px;"></i>
                        <h4>No products available</h4>
                        <p class="text-muted">Check back later for new products!</p>
                    </div>
                </div>
            `;
            return;
        }

        console.log(`Displaying ${products.length} products`);
        products.forEach((product, index) => {
            console.log(`Creating product card for: ${product.name}`);
            
            // Safely format price
            const price = typeof product.price === 'number' ? product.price.toFixed(2) : '0.00';
            
            // Use actual image URL from database or fallback to placeholder
            const imageUrl = product.imageUrl || `https://via.placeholder.com/300x200?text=${encodeURIComponent(product.name)}`;
            
            // Create product card with safe HTML
            const productCard = document.createElement('div');
            productCard.className = 'col-md-6 col-lg-4';
            productCard.innerHTML = `
                <div class="product-card">
                    <div class="product-image" style="background-image: url('${imageUrl}')"></div>
                    <div class="product-info">
                        <h5 class="product-title">${this.escapeHtml(product.name)}</h5>
                        <p class="product-description">${this.escapeHtml(product.description)}</p>
                        <div class="product-price">$${price}</div>
                        
                        <div class="row">
                            <div class="col-4">
                                <input type="number" value="1" min="1" class="form-control form-control-sm quantity-input" style="width: 100%;">
                            </div>
                            <div class="col-8">
                                <button class="btn btn-primary btn-add-cart w-100" onclick="app.addToCart(${product.id}, this.parentElement.previousElementSibling.querySelector('.quantity-input').value)">
                                    <i class="fas fa-cart-plus me-1"></i>Add to Cart
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            container.appendChild(productCard);
        });
        console.log('Products displayed successfully');
    }

    // Helper function to escape HTML
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }

    async addToCart(productId, quantity = 1) {
        if (!this.token) {
            this.showAlert('products-alert', 'Please login to add items to cart', 'danger');
            return;
        }

        try {
            const response = await this.authenticatedRequest('/api/v1/cart/add', {
                method: 'POST',
                body: JSON.stringify({ productId, quantity: parseInt(quantity) })
            });

            if (response.ok) {
                this.showAlert('products-alert', 'Item added to cart!', 'success');
                this.loadCart();
            } else {
                const error = await response.text();
                this.showAlert('products-alert', 'Failed to add to cart: ' + error, 'danger');
            }
        } catch (error) {
            this.showAlert('products-alert', 'Failed to add to cart: ' + error.message, 'danger');
        }
    }

    async loadCart() {
        if (!this.token) return;

        try {
            const response = await this.authenticatedRequest('/api/v1/cart');
            if (response.ok) {
                const cartData = await response.json();
                // Backend returns List<CartItemDto> directly, not wrapped in items property
                this.cart = Array.isArray(cartData) ? cartData : [];
                console.log('Cart loaded:', this.cart);
                this.displayCart();
                this.updateCartCount();
            } else {
                console.error('Failed to load cart, status:', response.status);
            }
        } catch (error) {
            console.error('Failed to load cart:', error);
        }
    }

    displayCart() {
        const container = document.getElementById('cart-items');
        const emptyCart = document.getElementById('empty-cart');
        const checkoutBtn = document.getElementById('checkout-btn');
        
        if (!container) return;

        if (this.cart.length === 0) {
            container.innerHTML = '';
            if (emptyCart) emptyCart.style.display = 'block';
            if (checkoutBtn) checkoutBtn.style.display = 'none';
            return;
        }

        if (emptyCart) emptyCart.style.display = 'none';
        if (checkoutBtn) checkoutBtn.style.display = 'block';

        let total = 0;
        container.innerHTML = '';

        this.cart.forEach(item => {
            // Handle both data structures - check if product is nested or flat
            const productName = item.productName || (item.product ? item.product.name : 'Unknown Product');
            const productDescription = item.productDescription || (item.product ? item.product.description : '');
            const productPrice = item.productPrice || (item.product ? item.product.price : 0);
            
            // Safely format prices
            const price = typeof productPrice === 'number' ? productPrice.toFixed(2) : '0.00';
            const itemTotal = item.quantity * (typeof productPrice === 'number' ? productPrice : 0);
            const itemTotalFormatted = typeof itemTotal === 'number' ? itemTotal.toFixed(2) : '0.00';
            total += itemTotal;

            const cartItem = document.createElement('div');
            cartItem.className = 'cart-item';
            cartItem.innerHTML = `
                <div class="row align-items-center">
                    <div class="col-md-4">
                        <h6 class="mb-1">${this.escapeHtml(productName)}</h6>
                        <p class="text-muted mb-0">${this.escapeHtml(productDescription)}</p>
                    </div>
                    <div class="col-md-2">
                        <span class="fw-bold">$${price}</span>
                    </div>
                    <div class="col-md-2">
                        <input type="number" value="${item.quantity}" min="1" class="form-control quantity-input" 
                               onchange="app.updateQuantity(${item.id}, this.value)">
                    </div>
                    <div class="col-md-2">
                        <span class="fw-bold">$${itemTotalFormatted}</span>
                    </div>
                    <div class="col-md-2">
                        <button class="btn btn-remove" onclick="app.removeFromCart(${item.id})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </div>
                </div>
            `;
            container.appendChild(cartItem);
        });

        const cartSubtotal = document.getElementById('cart-subtotal');
        const cartTotal = document.getElementById('cart-total');
        const totalFormatted = typeof total === 'number' ? total.toFixed(2) : '0.00';
        if (cartSubtotal) cartSubtotal.textContent = `$${totalFormatted}`;
        if (cartTotal) cartTotal.textContent = `$${totalFormatted}`;
        
        // Store the subtotal for discount calculations
        this.cartSubtotal = total;
    }

    async updateQuantity(itemId, newQuantity) {
        if (newQuantity < 1) return;

        try {
            const response = await this.authenticatedRequest(`/api/v1/cart/${itemId}`, {
                method: 'PUT',
                body: JSON.stringify({ quantity: parseInt(newQuantity) })
            });

            if (response.ok) {
                this.loadCart();
            } else {
                const error = await response.text();
                this.showAlert('products-alert', 'Failed to update quantity: ' + error, 'danger');
            }
        } catch (error) {
            this.showAlert('products-alert', 'Failed to update quantity: ' + error.message, 'danger');
        }
    }

    async removeFromCart(itemId) {
        try {
            const response = await this.authenticatedRequest(`/api/v1/cart/${itemId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                this.showAlert('products-alert', 'Item removed from cart', 'success');
                this.loadCart();
            } else {
                const error = await response.text();
                this.showAlert('products-alert', 'Failed to remove item: ' + error, 'danger');
            }
        } catch (error) {
            this.showAlert('products-alert', 'Failed to remove item: ' + error.message, 'danger');
        }
    }

    async loadOrders() {
        if (!this.token) return;

        try {
            const response = await this.authenticatedRequest('/api/v1/orders');
            if (response.ok) {
                const orders = await response.json();
                this.displayOrders(orders);
            }
        } catch (error) {
            this.showAlert('orders-alert', 'Failed to load orders: ' + error.message, 'danger');
        }
    }

    displayOrders(orders) {
        const container = document.getElementById('orders-list');
        if (!container) return;

        if (orders.length === 0) {
            container.innerHTML = `
                <div class="text-center">
                    <i class="fas fa-receipt" style="font-size: 4rem; color: #ccc; margin-bottom: 20px;"></i>
                    <h4>No orders yet</h4>
                    <p class="text-muted">Start shopping to see your orders here!</p>
                </div>
            `;
            return;
        }

        container.innerHTML = '';
        orders.forEach(order => {
            const orderDate = new Date(order.orderDate).toLocaleDateString();
            const totalAmount = typeof order.totalAmount === 'number' ? order.totalAmount.toFixed(2) : '0.00';
            
            const orderCard = document.createElement('div');
            orderCard.className = 'order-card';
            orderCard.innerHTML = `
                <div class="row">
                    <div class="col-md-6">
                        <h5>Order #${order.id}</h5>
                        <p class="text-muted mb-0">Date: ${orderDate}</p>
                        <p class="text-muted mb-0">Status: <span class="badge bg-primary">${this.escapeHtml(order.status)}</span></p>
                    </div>
                    <div class="col-md-6 text-end">
                        <h4 class="text-primary">$${totalAmount}</h4>
                        <button class="btn btn-outline-primary btn-sm" onclick="app.viewOrderDetails(${order.id})">
                            View Details
                        </button>
                    </div>
                </div>
            `;
            container.appendChild(orderCard);
        });
    }

    async viewOrderDetails(orderId) {
        try {
            const response = await this.authenticatedRequest(`/api/v1/orders/${orderId}`);
            if (response.ok) {
                const order = await response.json();
                this.showOrderDetails(order);
            }
        } catch (error) {
            this.showAlert('orders-alert', 'Failed to load order details: ' + error.message, 'danger');
        }
    }

    showOrderDetails(order) {
        // Defensive: accept either order.orderItems (API you showed) or order.items (old shape) 
        const items = Array.isArray(order.orderItems) ? order.orderItems 
                     : Array.isArray(order.items) ? order.items 
                     : []; 
    
        // Build HTML rows for each item defensively 
        let itemsHtml = ''; 
        items.forEach(item => { 
            // Normalize fields from possible shapes 
            const name = this.escapeHtml(item.productName || (item.product && item.product.name) || 'Product'); 
            const quantity = Number(item.quantity || item.qty || (item.product && item.quantity) || 0); 
            const price = Number(item.price || (item.product && item.product.price) || 0); 
            const subtotal = Number(item.totalPrice || (item.totalPrice === 0 ? 0 : (price * quantity)) || 0); 
    
            itemsHtml += ` 
                <tr> 
                    <td>${name}</td> 
                    <td class="text-center">${quantity}</td> 
                    <td class="text-end">$${price.toFixed(2)}</td> 
                    <td class="text-end">$${subtotal.toFixed(2)}</td> 
                </tr> 
            `; 
        }); 
    
        // Ensure safe date and amounts 
        const orderDate = order.orderDate ? new Date(order.orderDate).toLocaleDateString() : '—'; 
        const totalAmount = Number(order.totalAmount || order.total || 0);
        
        const modal = `
            <div class="modal fade" id="orderDetailsModal" tabindex="-1"> 
                <div class="modal-dialog modal-lg"> 
                    <div class="modal-content"> 
                        <div class="modal-header"> 
                            <h5 class="modal-title">Order #${order.id} Details</h5> 
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button> 
                        </div> 
                        <div class="modal-body"> 
                            <div class="row mb-3"> 
                                <div class="col-md-6"> 
                                    <strong>Order Date:</strong> ${orderDate}<br> 
                                    <strong>Status:</strong> <span class="badge bg-primary">${this.escapeHtml(order.status || '—')}</span> 
                                    ${order.discountCode ? `<br><strong>Discount Code:</strong> ${this.escapeHtml(order.discountCode)}` : ''}
                                </div> 
                                <div class="col-md-6 text-end"> 
                                    <strong>Subtotal:</strong> $${Number(order.subtotal || 0).toFixed(2)}<br>
                                    ${order.discountAmount && Number(order.discountAmount) > 0 ? `<strong>Discount:</strong> -$${Number(order.discountAmount).toFixed(2)}<br>` : ''}
                                    <strong>Total Amount:</strong> <span class="text-primary">$${totalAmount.toFixed(2)}</span> 
                                </div> 
                            </div> 
                            <h6>Order Items:</h6> 
                            <div class="table-responsive"> 
                                <table class="table table-sm"> 
                                    <thead> 
                                        <tr> 
                                            <th>Product</th> 
                                            <th class="text-center">Quantity</th> 
                                            <th class="text-end">Price</th> 
                                            <th class="text-end">Subtotal</th> 
                                        </tr> 
                                    </thead> 
                                    <tbody> 
                                        ${itemsHtml || `<tr><td colspan="4" class="text-center text-muted">No items found</td></tr>`} 
                                    </tbody> 
                                </table> 
                            </div> 
                            ${order.discountCode && Number(order.discountAmount) > 0 ? `<div class="mt-3"><strong>Discount Applied:</strong> ${this.escapeHtml(order.discountCode)} (-$${Number(order.discountAmount).toFixed(2)})</div>` : ''} 
                        </div> 
                    </div> 
                </div> 
            </div> 
        `;
        
        // Remove existing modal if any, then append and show new modal
        const existingModal = document.getElementById('orderDetailsModal');
        if (existingModal) {
            existingModal.remove();
        }
        
        // Add new modal to body
        document.body.insertAdjacentHTML('beforeend', modal);
        
        // Show modal
        const modalElement = document.getElementById('orderDetailsModal');
        const bootstrapModal = new bootstrap.Modal(modalElement);
        bootstrapModal.show();
        
        // Clean up modal when hidden
        modalElement.addEventListener('hidden.bs.modal', () => {
            modalElement.remove();
        });
    }

    async checkout() {
        if (this.cart.length === 0) {
            this.showAlert('products-alert', 'Your cart is empty', 'danger');
            return;
        }

        try {
            const discountCode = this.currentDiscountCode || null;
            const requestBody = {
                discountCode: discountCode
            };
            
            const response = await this.authenticatedRequest('/api/v1/orders', {
                method: 'POST',
                body: JSON.stringify(requestBody)
            });

            if (response.ok) {
                const order = await response.json();
                this.showAlert('products-alert', `Order placed successfully! Order #${order.id}`, 'success');
                this.cart = [];
                this.currentDiscountCode = null;
                this.clearDiscount();
                this.loadCart();
                this.showOrders();
            } else {
                const error = await response.text();
                this.showAlert('products-alert', 'Checkout failed: ' + error, 'danger');
            }
        } catch (error) {
            this.showAlert('products-alert', 'Checkout failed: ' + error.message, 'danger');
        }
    }

    updateCartCount() {
        const count = this.cart.reduce((total, item) => total + item.quantity, 0);
        const cartCount = document.getElementById('cart-count');
        if (cartCount) cartCount.textContent = count;
    }

    async authenticatedRequest(url, options = {}) {
        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${this.token}`,
            ...options.headers
        };

        return fetch(url, {
            ...options,
            headers
        });
    }

    showAlert(elementId, message, type) {
        const alertElement = document.getElementById(elementId);
        if (!alertElement) {
            console.log(`Alert element ${elementId} not found`);
            return;
        }
        
        alertElement.textContent = message;
        alertElement.className = `alert alert-${type}`;
        alertElement.style.display = 'block';
        
        // Auto-hide after 5 seconds
        setTimeout(() => {
            alertElement.style.display = 'none';
        }, 5000);
    }

    // This function fetches discounts and creates buttons for them
    async fetchAndDisplayDiscounts() {
        const container = document.getElementById('discount-buttons-container');
        if (!container) return;
        container.innerHTML = ''; // Clear previous buttons

        try {
            const response = await this.authenticatedRequest('/api/v1/discounts', {
                method: 'GET'
            });

            if (!response.ok) {
                throw new Error('Failed to fetch discounts');
            }

            const discounts = await response.json();

            // If no discounts, display a message
            if (!discounts || discounts.length === 0) {
                container.innerHTML = '<p class="text-white-50 small">No coupons available right now.</p>';
                return;
            }

            // Create buttons for the first 3 discounts
            discounts.slice(0, 3).forEach(discount => {
                const button = document.createElement('button');
                button.type = 'button';
                button.className = 'btn btn-sm btn-outline-light me-2 mb-2';
                button.textContent = discount.code;
                button.title = discount.description || discount.code; // Add description on hover
                button.onclick = () => this.applyDiscountFromButton(discount.code);
                container.appendChild(button);
            });

        } catch (error) {
            console.error('Error fetching discounts:', error);
            container.innerHTML = '<p class="text-white-50 small">Could not load coupons.</p>';
        }
    }

    // This helper function applies a discount code from a button click
    applyDiscountFromButton(code) {
        const discountInput = document.getElementById('discount-code');
        if (discountInput) {
            discountInput.value = code;
            this.applyDiscount(); // This calls your existing function to validate and apply the discount
        }
    }

    // Navigation functions
    showProducts() {
        console.log('Showing products page...');
        this.hideAllPagesExcept('products-page');
        const productsPage = document.getElementById('products-page');
        const mainContent = document.getElementById('main-content');
        
        console.log('Main content display style:', mainContent ? mainContent.style.display : 'not found');
        console.log('Products page display style before:', productsPage ? productsPage.style.display : 'not found');
        
        if (productsPage) {
            console.log('Found products page, setting display to block');
            productsPage.style.display = 'block';
            console.log('Products page display style after:', productsPage.style.display);
            this.loadProducts();
            this.updateActiveNav('products');
        } else {
            console.log('Products page not found');
        }
    }

    async showCart() {
        console.log('Showing cart page...');
        this.hideAllPagesExcept('cart-page');
        const cartPage = document.getElementById('cart-page');
        if (cartPage) {
            cartPage.style.display = 'block';
            await this.loadCart();
            this.updateActiveNav('cart');
            
            // Fetch and display available discount coupons
            await this.fetchAndDisplayDiscounts();
        }
    }

    showOrders() {
        console.log('Showing orders page...');
        this.hideAllPagesExcept('orders-page');
        const ordersPage = document.getElementById('orders-page');
        if (ordersPage) {
            ordersPage.style.display = 'block';
            this.loadOrders();
            this.updateActiveNav('orders');
        }
    }

    showProfile() {
        console.log('Showing profile page...');
        this.hideAllPagesExcept('profile-page');
        const profilePage = document.getElementById('profile-page');
        if (profilePage) {
            profilePage.style.display = 'block';
            this.loadUserProfile();
            this.updateActiveNav('profile');
        }
    }

    hideAllPages() {
        console.log('Hiding all pages...');
        const pages = ['products-page', 'cart-page', 'orders-page', 'profile-page'];
        pages.forEach(id => {
            const page = document.getElementById(id);
            if (page) {
                page.style.display = 'none';
                console.log(`Hidden ${id}`);
            } else {
                console.log(`${id} not found`);
            }
        });
    }

    hideAllPagesExcept(exceptPageId) {
        console.log('Hiding all pages except:', exceptPageId);
        const pages = ['products-page', 'cart-page', 'orders-page', 'profile-page'];
        pages.forEach(pageId => {
            if (pageId !== exceptPageId) {
                const page = document.getElementById(pageId);
                if (page) {
                    page.style.display = 'none';
                    console.log('Hidden', pageId);
                }
            }
        });
    }

    updateActiveNav(section) {
        const navLinks = document.querySelectorAll('.navbar-nav .nav-link');
        navLinks.forEach(link => link.classList.remove('active'));
        
        const activeLink = document.querySelector(`[onclick="show${section.charAt(0).toUpperCase() + section.slice(1)}()"]`);
        if (activeLink) {
            activeLink.classList.add('active');
        }
    }

    async applyDiscount() {
        const discountCode = document.getElementById('discount-code').value.trim();
        const discountMessage = document.getElementById('discount-message');
        const discountRow = document.getElementById('discount-row');
        const discountAmount = document.getElementById('discount-amount');
        const cartTotal = document.getElementById('cart-total');
        
        if (!discountCode) {
            this.showDiscountMessage('Please enter a discount code', 'warning');
            return;
        }
        
        if (!this.cartSubtotal || this.cartSubtotal <= 0) {
            this.showDiscountMessage('Cart is empty', 'warning');
            return;
        }
        
        try {
            const response = await this.authenticatedRequest(`/api/v1/discounts/validate?code=${encodeURIComponent(discountCode)}&amount=${this.cartSubtotal}`);
            
            if (response.ok) {
                const discountData = await response.json();
                if (discountData.valid) {
                    const discountValue = discountData.discountAmount || 0;
                    const newTotal = this.cartSubtotal - discountValue;
                    
                    // Show discount row
                    discountRow.style.display = 'flex';
                    discountAmount.textContent = `-$${discountValue.toFixed(2)}`;
                    cartTotal.textContent = `$${newTotal.toFixed(2)}`;
                    
                    this.showDiscountMessage(`Discount applied! ${discountData.description || ''}`, 'success');
                    this.currentDiscountCode = discountCode;
                } else {
                    this.showDiscountMessage(discountData.message || 'Invalid discount code', 'danger');
                    this.clearDiscount();
                }
            } else {
                const error = await response.text();
                this.showDiscountMessage('Failed to apply discount: ' + error, 'danger');
                this.clearDiscount();
            }
        } catch (error) {
            this.showDiscountMessage('Failed to apply discount: ' + error.message, 'danger');
            this.clearDiscount();
        }
    }
    
    clearDiscount() {
        const discountRow = document.getElementById('discount-row');
        const discountAmount = document.getElementById('discount-amount');
        const cartTotal = document.getElementById('cart-total');
        
        discountRow.style.display = 'none';
        discountAmount.textContent = '-$0.00';
        cartTotal.textContent = `$${this.cartSubtotal ? this.cartSubtotal.toFixed(2) : '0.00'}`;
        this.currentDiscountCode = null;
    }
    
    showDiscountMessage(message, type) {
        const discountMessage = document.getElementById('discount-message');
        if (discountMessage) {
            discountMessage.textContent = message;
            discountMessage.className = `form-text text-white text-${type}`;
            discountMessage.style.display = 'block';
            
            // Auto-hide after 5 seconds
            setTimeout(() => {
                discountMessage.style.display = 'none';
            }, 5000);
        }
    }
}

// Global variables and functions
let app;

// Initialize app when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM loaded, initializing app...');
    app = new EcommerceApp();
    app.init();
});

// Global functions for navigation
function showProducts() { 
    console.log('Global showProducts called');
    if (app) {
        console.log('App found, calling showProducts');
        app.showProducts(); 
    } else {
        console.log('App not found');
    }
}

function showCart() { 
    console.log('Global showCart called');
    if (app) {
        console.log('App found, calling showCart');
        app.showCart(); 
    } else {
        console.log('App not found');
    }
}

function showOrders() { 
    console.log('Global showOrders called');
    if (app) {
        console.log('App found, calling showOrders');
        app.showOrders(); 
    } else {
        console.log('App not found');
    }
}

function showProfile() { 
    console.log('Global showProfile called');
    if (app) {
        console.log('App found, calling showProfile');
        app.showProfile(); 
    } else {
        console.log('App not found');
    }
}

function logout() { 
    console.log('Global logout called');
    if (app) {
        console.log('App found, calling logout');
        app.logout(); 
    } else {
        console.log('App not found');
    }
}

function checkout() { 
    console.log('Global checkout called');
    if (app) {
        console.log('App found, calling checkout');
        app.checkout(); 
    } else {
        console.log('App not found');
    }
}

function applyDiscount() {
    console.log('Global applyDiscount called');
    if (app) {
        console.log('App found, calling applyDiscount');
        app.applyDiscount();
    } else {
        console.log('App not found');
    }
}