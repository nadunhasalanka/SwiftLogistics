// Helper to store user in localStorage
export function storeUserInLocalStorage(user: any) {
  if (user && user.token && user.name && user.id) {
    localStorage.setItem('user', JSON.stringify(user));
  }
}

// Signup API call
export async function signup({ email, password, name }: { email: string; password: string; name: string }) {
  const response = await fetch('http://localhost:4010/signup', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password, name }),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Signup failed');
  }

  const user = await response.json();
  storeUserInLocalStorage(user);
  return user;
}

// Login API call
export async function login({ email, password }: { email: string; password: string }) {
  const response = await fetch('http://localhost:4010/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password }),
  });

  if (!response.ok) {
    const errorData = await response.json();
    throw new Error(errorData.message || 'Login failed');
  }

  const user = await response.json();
  storeUserInLocalStorage(user);
  return user;
}

export function getCurrentUser() {
  try {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      return JSON.parse(userStr);
    }
  } catch (error) {
    console.error('Error parsing user from localStorage:', error);
  }
  return null;
}

// Helper to get current user's token
export function getCurrentUserToken() {
  const user = getCurrentUser();
  return user?.token || null;
}

// Helper to check if user is logged in
export function isUserLoggedIn() {
  const user = getCurrentUser();
  return !!(user && user.token && user.name && user.id);
}

// Helper to clear localStorage
export function logout() {
  localStorage.removeItem('user');
}