import { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('blog_user');
    return saved ? JSON.parse(saved) : null;
  });
  const [token, setToken] = useState(() => localStorage.getItem('blog_token'));

  function login(userData, userToken) {
    localStorage.setItem('blog_user', JSON.stringify(userData));
    localStorage.setItem('blog_token', userToken);
    setUser(userData);
    setToken(userToken);
  }

  function logout() {
    localStorage.removeItem('blog_user');
    localStorage.removeItem('blog_token');
    setUser(null);
    setToken(null);
  }

  return (
    <AuthContext.Provider value={{ user, token, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
