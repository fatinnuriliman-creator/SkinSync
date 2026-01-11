(function(){
    const CART_KEY = 'skinsync_cart';
    
    // --- 1. HELPER FUNCTIONS ---
    function slug(s){ return s.toLowerCase().replace(/[^\w]+/g,'-'); }
    function getCart(){ return JSON.parse(localStorage.getItem(CART_KEY) || '[]'); }
    function saveCart(c){ localStorage.setItem(CART_KEY, JSON.stringify(c)); updateCartBadge(); }
    
    function updateCartBadge(){
        const cart = getCart();
        const count = cart.reduce((s,i)=>s+(i.qty||0),0);
        document.querySelectorAll('.cart-count').forEach(el => el.textContent = count);
    }

    // --- 2. MAIN LOGIC (Bila page loading siap) ---
    document.addEventListener('DOMContentLoaded', () => {
        updateCartBadge();

        // A. LOGIC ADD TO CART (Untuk Button '+')
        const addToCartBtns = document.querySelectorAll('.btn-icon, .btn-primary');

        addToCartBtns.forEach(btn => {
            // Cuma aktifkan kalau button tu memang untuk add to cart
            if(btn.textContent.includes('+') || btn.textContent.toLowerCase().includes('add to cart')){
                
                btn.addEventListener('click', (e)=>{
                    e.preventDefault(); 
                    
                    const card = e.target.closest('.product-card');
                    if(!card) return; 

                    // Ambil data produk
                    const name = card.querySelector('h3')?.innerText?.trim() || 'Item';
                    const priceText = card.querySelector('.price')?.innerText || 'RM 0';
                    const price = parseFloat(priceText.replace(/[^0-9.]+/g,'')) || 0;
                    const img = card.querySelector('img')?.src || '';
                    const id = slug(name);
                    
                    // Simpan ke LocalStorage
                    const cart = getCart();
                    const existing = cart.find(i=>i.id===id);
                    if(existing) existing.qty += 1; 
                    else cart.push({id, name, price, qty:1, image: img});
                    
                    saveCart(cart);
                    
                    // Animasi Button
                    const originalText = btn.innerText;
                    btn.innerText = '✓'; 
                    btn.style.backgroundColor = '#2f3e3b'; 
                    btn.style.color = '#fff';

                    setTimeout(()=> {
                        btn.innerText = originalText;
                        btn.style.backgroundColor = '';
                        btn.style.color = '';
                    }, 1000);
                });
            }
        });

        // B. BURGER MENU (Mobile)
        const burger = document.querySelector('.burger');
        const nav = document.querySelector('.nav-links');
        
        if(burger && nav){
            burger.addEventListener('click', () => {
                nav.classList.toggle('nav-active');
                burger.classList.toggle('toggle');
            });
        }

        // C. NAVBAR SCROLL EFFECT
        const navbar = document.querySelector('.navbar');
        if(navbar) {
            window.addEventListener('scroll', () => {
                if(window.scrollY > 50) {
                    navbar.style.boxShadow = "0 5px 20px rgba(0,0,0,0.1)";
                    navbar.style.padding = "15px 5%"; 
                } else {
                    navbar.style.boxShadow = "0 2px 10px rgba(0,0,0,0.05)";
                    navbar.style.padding = "20px 5%";
                }
            });
        }

        // D. KALAU KITA DI PAGE CART, JALANKAN RENDER CART
        if(document.getElementById('cartItems')) {
            renderCart();
        }
    });

    // --- 3. FUNGSI RENDER CART YANG DITAMBAH LOGIK SHIPPING ---
        function renderCart(){
            const cart = getCart();
            const container = document.getElementById('cartItems');
            
            // Element Harga
            const subtotalEl = document.getElementById('subtotal');
            const shippingEl = document.getElementById('shipping');
            const totalEl = document.getElementById('total');

            if(!container) return;
            container.innerHTML = '';

            // Kalau cart kosong
            if(cart.length === 0){
                container.innerHTML = `
                    <div style="text-align:center; padding: 40px;">
                        <h3 style="color:#ccc;">Your bag is empty</h3>
                        <a href="index.html" class="btn btn-primary" style="margin-top:20px; display:inline-block;">Shop Now</a>
                    </div>`;
                if(subtotalEl) subtotalEl.textContent = 'RM 0.00';
                if(shippingEl) shippingEl.textContent = 'RM 0.00';
                if(totalEl) totalEl.textContent = 'RM 0.00';
                return;
            }

            let subtotal = 0;
            
            // Loop barang...
            cart.forEach(item => {
                subtotal += item.price * item.qty;
                
                const div = document.createElement('div'); 
                div.className = 'cart-item';
                div.innerHTML = `
                    <div style="display:flex; align-items:center;">
                        <img src="${item.image}" alt="${item.name}">
                        <div class="item-details">
                            <h4>${item.name}</h4>
                            <div class="item-price">RM ${item.price.toFixed(2)}</div>
                        </div>
                    </div>
                    <div style="display:flex; align-items:center;">
                        <div class="qty-control">
                            <button class="qty-btn qty-decrease">-</button>
                            <span class="qty-val">${item.qty}</span>
                            <button class="qty-btn qty-increase">+</button>
                        </div>
                        <button class="remove-btn">&times;</button>
                    </div>
                `;
                
                div.querySelector('.qty-increase').addEventListener('click', ()=>{ item.qty++; saveCart(cart); renderCart(); });
                div.querySelector('.qty-decrease').addEventListener('click', ()=>{ if(item.qty>1){ item.qty--; saveCart(cart); renderCart(); } });
                div.querySelector('.remove-btn').addEventListener('click', ()=>{ 
                    const idx = cart.findIndex(i=>i.id===item.id); 
                    if(idx>-1){ cart.splice(idx,1); saveCart(cart); renderCart(); } 
                });
                
                container.appendChild(div);
            });

            // --- LOGIK PENGIRAAN TOTAL PAYMENT ---
            let shippingCost = 10.00; // Default shipping RM 10
            if(subtotal >= 100) {
                shippingCost = 0.00; // Free shipping kalau beli > RM 100
            }

            let grandTotal = subtotal + shippingCost;

            // Update Paparan HTML
            if(subtotalEl) subtotalEl.textContent = 'RM ' + subtotal.toFixed(2);
            
            if(shippingEl) {
                shippingEl.textContent = shippingCost === 0 ? 'Free' : 'RM ' + shippingCost.toFixed(2);
                shippingEl.style.color = shippingCost === 0 ? '#27ae60' : '#666'; // Warna hijau kalau free
            }
            
            if(totalEl) totalEl.textContent = 'RM ' + grandTotal.toFixed(2);

            // Update Hidden Input (Untuk dihantar ke Java Servlet)
            const hiddenTotal = document.getElementById('hiddenTotalPayment');
            if(hiddenTotal) hiddenTotal.value = grandTotal.toFixed(2);
        }    

    // Dedahkan fungsi cart untuk kegunaan luar
    window.skinsync = { getCart, saveCart };
})();