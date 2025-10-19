document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('loginForm');
    const signupForm = document.getElementById('signupForm');

    if (loginForm) {
        loginForm.addEventListener('submit', (event) => {
            event.preventDefault();
            alert('Login form submitted!');
            // Here you would typically send data to your backend
            // e.g., fetch('/api/login', { method: 'POST', body: new FormData(loginForm) })
        });
    }

    if (signupForm) {
        signupForm.addEventListener('submit', (event) => {
            event.preventDefault();
            const password = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (password !== confirmPassword) {
                alert('Passwords do not match!');
                return;
            }
            alert('Sign Up form submitted!');
            // Here you would typically send data to your backend
            // e.g., fetch('/api/signup', { method: 'POST', body: new FormData(signupForm) })
        });
    }
});
