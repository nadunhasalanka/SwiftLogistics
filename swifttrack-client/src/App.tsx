import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { OrderProvider } from './context/OrderContext';
import { Navigation } from './components/Navigation';
import { Login } from './components/Login';
import { Register } from './components/Register';
import { Dashboard } from './components/Dashboard';
import { CreateOrderForm } from './components/CreateOrderForm';
import { OrdersList } from './components/OrdersList';
import './App.css';

// Layout component to conditionally show navigation
function AppLayout({ children }: { children: React.ReactNode }) {
  const location = useLocation();
  const isAuthPage = location.pathname === '/login' || location.pathname === '/register';

  const handleLogout = () => {
    // Add logout logic here
    console.log('Logout clicked');
    window.location.href = '/login';
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {!isAuthPage && <Navigation onLogout={handleLogout} />}
      <main className={isAuthPage ? '' : 'py-6'}>
        {children}
      </main>
    </div>
  );
}

function App() {
  return (
    <OrderProvider>
      <Router>
        <AppLayout>
          <Routes>
            {/* Authentication Routes */}
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            
            {/* Protected Routes */}
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/create-order" element={<CreateOrderForm />} />
            <Route path="/orders" element={<OrdersList />} />
            
            {/* Redirect root to dashboard */}
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
            
            {/* Catch all route */}
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </AppLayout>
        
        <Toaster 
          position="top-right"
          toastOptions={{
            duration: 4000,
            style: {
              background: '#363636',
              color: '#fff',
            },
            success: {
              duration: 3000,
              style: {
                background: '#10b981',
              },
            },
            error: {
              duration: 4000,
              style: {
                background: '#ef4444',
              },
            },
          }}
        />
      </Router>
    </OrderProvider>
  );
}

export default App;