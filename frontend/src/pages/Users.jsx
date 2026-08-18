import { useEffect, useState } from 'react'
import apiClient from '../api/client'

const emptyForm = { username: '', password: '', fullName: '', role: 'STAFF' }

export default function Users() {
  const [users, setUsers] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState(emptyForm)
  const [error, setError] = useState('')

  async function load() {
    const res = await apiClient.get('/users')
    setUsers(res.data)
  }

  useEffect(() => { load() }, [])

  function openNew() {
    setForm(emptyForm)
    setError('')
    setShowForm(true)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    try {
      await apiClient.post('/users', form)
      setShowForm(false)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not create user')
    }
  }

  async function handleDelete(id) {
    if (!window.confirm('Remove this user account? They will no longer be able to log in.')) return
    await apiClient.delete(`/users/${id}`)
    load()
  }

  return (
    <div>
      <div className="page-header">
        <h1>System Users</h1>
        <button className="btn btn-primary" onClick={openNew}>+ Add User</button>
      </div>
      <p style={{ color: '#6b7280', marginTop: -12, marginBottom: 20 }}>
        Only Admins can see this page. Add a login account for another staff
        member here - they'll use the username and password to sign in.
      </p>

      {users.length === 0 ? (
        <div className="empty-state">No users yet.</div>
      ) : (
        <table>
          <thead>
            <tr><th>Username</th><th>Full Name</th><th>Role</th><th>Status</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {users.map((u) => (
              <tr key={u.id}>
                <td>{u.username}</td>
                <td>{u.fullName}</td>
                <td><span className={'status ' + (u.role === 'ADMIN' ? 'status-APPROVED' : 'status-PENDING')}>{u.role}</span></td>
                <td>{u.enabled ? 'Active' : 'Disabled'}</td>
                <td className="actions">
                  <button className="btn btn-danger btn-sm" onClick={() => handleDelete(u.id)}>Remove</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {showForm && (
        <div style={{ marginTop: 20 }}>
          <h3>Add User</h3>
          {error && <div className="alert">{error}</div>}
          <form className="form-box" onSubmit={handleSubmit}>
            <div className="field">
              <label>Full Name</label>
              <input value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} required />
            </div>
            <div className="field">
              <label>Username</label>
              <input value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} required />
            </div>
            <div className="field">
              <label>Password</label>
              <input type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required minLength={4} />
            </div>
            <div className="field">
              <label>Role</label>
              <select value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })}>
                <option value="STAFF">STAFF</option>
                <option value="ADMIN">ADMIN</option>
              </select>
            </div>
            <button type="submit" className="btn btn-primary">Create User</button>
            <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)} style={{ marginLeft: 8 }}>Cancel</button>
          </form>
        </div>
      )}
    </div>
  )
}
