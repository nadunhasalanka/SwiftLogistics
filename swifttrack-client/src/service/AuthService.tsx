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