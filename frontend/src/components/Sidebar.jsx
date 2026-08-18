import { NavLink } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

const linkClass = ({ isActive }) => 'sidebar-link' + (isActive ? ' active' : '')

export default function Sidebar() {
  const { user, isAdmin, logout } = useAuth()

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <span className="sidebar-brand-icon">🏠</span>
        <div>
          <div className="sidebar-brand-title">Hostel Management</div>
          <div className="sidebar-brand-subtitle">System</div>
        </div>
      </div>

      <nav className="sidebar-nav">
        <NavLink to="/" end className={linkClass}><span className="icon">📊</span> Dashboard</NavLink>
        <NavLink to="/hostels" className={linkClass}><span className="icon">🏢</span> Hostels</NavLink>
        <NavLink to="/rooms" className={linkClass}><span className="icon">🚪</span> Rooms</NavLink>
        <NavLink to="/bookings" className={linkClass}><span className="icon">📝</span> Bookings</NavLink>
        <NavLink to="/students" className={linkClass}><span className="icon">🎓</span> Students</NavLink>
        <NavLink to="/wardens" className={linkClass}><span className="icon">🛡️</span> Wardens</NavLink>
        {isAdmin && (
          <NavLink to="/users" className={linkClass}><span className="icon">👤</span> Users</NavLink>
        )}
      </nav>

      <div className="sidebar-footer">
        <div style={{ marginBottom: 8 }}>
          {user?.fullName} <span style={{ opacity: 0.7 }}>({user?.role})</span>
        </div>
        <button className="btn btn-secondary btn-sm" onClick={logout}>Log out</button>
      </div>
    </aside>
  )
}
