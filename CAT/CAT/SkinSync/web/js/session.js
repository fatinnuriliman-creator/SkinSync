// Session Management Script for SkinSync
// This script checks user login status and updates the navbar accordingly

(function() {
    'use strict';

    // Check session status on page load
    function checkSession() {
        fetch('SessionCheckServlet')
            .then(response => response.json())
            .then(data => {
                updateNavbar(data);
            })
            .catch(err => {
                console.error('Error checking session:', err);
                // If error, assume not logged in
                updateNavbar({ loggedIn: false });
            });
    }

    // Update navbar based on session status
    function updateNavbar(sessionData) {
        const navLinks = document.querySelector('.nav-links');
        if (!navLinks) return;

        // Find the login button
        const loginBtn = navLinks.querySelector('.btn-login');
        
        if (sessionData.loggedIn) {
            // User is logged in - show logout button and user name
            if (loginBtn) {
                loginBtn.textContent = 'Logout';
                loginBtn.href = 'LogoutServlet';
                loginBtn.style.background = '#ff6b6b';
                
                // Add user name before logout button
                const userNameLi = document.createElement('li');
                userNameLi.innerHTML = `<span style="color: var(--primary); font-weight: 600;">👤 ${sessionData.fullName}</span>`;
                loginBtn.parentElement.parentElement.insertBefore(userNameLi, loginBtn.parentElement);
            }
        } else {
            // User is not logged in - show login button
            if (loginBtn) {
                loginBtn.textContent = 'Login';
                loginBtn.href = 'login.html';
                loginBtn.style.background = 'var(--text-dark)';
            }
        }
    }

    // Check for logout status message
    function checkLogoutStatus() {
        const urlParams = new URLSearchParams(window.location.search);
        const status = urlParams.get('status');
        
        if (status === 'logout') {
            // Show a temporary message
            const message = document.createElement('div');
            message.style.cssText = 'position: fixed; top: 80px; right: 20px; background: #27ae60; color: white; padding: 15px 25px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.15); z-index: 9999; animation: slideIn 0.3s ease;';
            message.textContent = '✓ Successfully logged out';
            document.body.appendChild(message);
            
            // Remove message after 3 seconds
            setTimeout(() => {
                message.style.animation = 'slideOut 0.3s ease';
                setTimeout(() => message.remove(), 300);
            }, 3000);
            
            // Clean URL
            window.history.replaceState({}, document.title, window.location.pathname);
        }
    }

    // Initialize on page load
    document.addEventListener('DOMContentLoaded', () => {
        checkSession();
        checkLogoutStatus();
    });

})();
