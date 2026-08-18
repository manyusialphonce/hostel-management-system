import { useEffect, useState } from 'react'
import apiClient from '../api/client'

const emptyForm = { fullName: '', email: '', phone: '', staffNumber: '', officeLocation: '' }

export default function Wardens() {
  const [wardens, setWardens] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState(emptyForm)
  const [editingId, setEditingId] = useState(null)
  const [error, setError] = useState('')

  async function load() {
    const res = await apiClient.get('/wardens')
    setWardens(res.data)
  }

  useEffect(() => { load() }, [])

  function openNew() {
    setForm(emptyForm)
    setEditingId(null)
    setError('')
    setShowForm(true)
  }

  function openEdit(warden) {
    setForm({
      fullName: warden.fullName, email: warden.email, phone: warden.phone,
      staffNumber: warden.staffNumber, officeLocation: warden.officeLocation || '',
    })
    setEditingId(warden.id)
    setError('')
    setShowForm(true)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    try {
      if (editingId) {
        await apiClient.put(`/wardens/${editingId}`, form)
      } else {
        await apiClient.post('/wardens', form)
      }
      setShowForm(false)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not save warden')
    }
  }

  async function handleDelete(id) {
    if (!window.confirm('Delete this warden?')) return
    await apiClient.delete(`/wardens/${id}`)
    load()
  }

  return (
    <div>
      <div className="page-header">
        <h1>Wardens</h1>
        <button className="btn btn-primary" onClick={openNew}>+ Add Warden</button>
      </div>

      {wardens.length === 0 ? (
        <div className="empty-state">No wardens yet. Click "Add Warden" to create one.</div>
      ) : (
        <table>
          <thead>
            <tr><th>Staff Number</th><th>Full Name</th><th>Office</th><th>Email</th><th>Phone</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {wardens.map((w) => (
              <tr key={w.id}>
                <td>{w.staffNumber}</td>
                <td>{w.fullName}</td>
                <td>{w.officeLocation}</td>
                <td>{w.email}</td>
                <td>{w.phone}</td>
                <td className="actions">
                  <button className="btn btn-secondary btn-sm" onClick={() => openEdit(w)}>Edit</button>
                  <button className="btn btn-danger btn-sm" onClick={() => handleDelete(w.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {showForm && (
        <div style={{ marginTop: 20 }}>
          <h3>{editingId ? 'Edit Warden' : 'Add Warden'}</h3>
          {error && <div className="alert">{error}</div>}
          <form className="form-box" onSubmit={handleSubmit}>
            <div className="field">
              <label>Full Name</label>
              <input value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} required />
            </div>
            <div className="field">
              <label>Staff Number</label>
              <input value={form.staffNumber} onChange={(e) => setForm({ ...form, staffNumber: e.target.value })} required />
            </div>
            <div className="field">
              <label>Office Location</label>
              <input value={form.officeLocation} onChange={(e) => setForm({ ...form, officeLocation: e.target.value })} />
            </div>
            <div className="field">
              <label>Email</label>
              <input type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required />
            </div>
            <div className="field">
              <label>Phone</label>
              <input value={form.phone} onChange={(e) => setForm({ ...form, phone: e.target.value })} required />
            </div>
            <button type="submit" className="btn btn-primary">Save</button>
            <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)} style={{ marginLeft: 8 }}>Cancel</button>
          </form>
        </div>
      )}
    </div>
  )
}
