import { useEffect, useState } from 'react'
import apiClient from '../api/client'

const emptyForm = { name: '', location: '', wardenId: '' }

export default function Hostels() {
  const [hostels, setHostels] = useState([])
  const [wardens, setWardens] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState(emptyForm)
  const [editingId, setEditingId] = useState(null)
  const [error, setError] = useState('')

  async function load() {
    const [hostelsRes, wardensRes] = await Promise.all([
      apiClient.get('/hostels'),
      apiClient.get('/wardens'),
    ])
    setHostels(hostelsRes.data)
    setWardens(wardensRes.data)
  }

  useEffect(() => { load() }, [])

  function openNew() {
    setForm(emptyForm)
    setEditingId(null)
    setError('')
    setShowForm(true)
  }

  function openEdit(hostel) {
    setForm({ name: hostel.name, location: hostel.location || '', wardenId: hostel.warden?.id || '' })
    setEditingId(hostel.id)
    setError('')
    setShowForm(true)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    try {
      const payload = { ...form, wardenId: form.wardenId ? Number(form.wardenId) : null }
      if (editingId) {
        await apiClient.put(`/hostels/${editingId}`, payload)
      } else {
        await apiClient.post('/hostels', payload)
      }
      setShowForm(false)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not save hostel')
    }
  }

  async function handleDelete(id) {
    if (!window.confirm('Delete this hostel?')) return
    await apiClient.delete(`/hostels/${id}`)
    load()
  }

  return (
    <div>
      <div className="page-header">
        <h1>Hostels</h1>
        <button className="btn btn-primary" onClick={openNew}>+ Add Hostel</button>
      </div>

      {hostels.length === 0 ? (
        <div className="empty-state">No hostels yet. Click "Add Hostel" to create one.</div>
      ) : (
        <table>
          <thead>
            <tr><th>Name</th><th>Location</th><th>Beds Free</th><th>Status</th><th>Warden</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {hostels.map((h) => {
              const isOpen = h.availableSpaces > 0
              return (
                <tr key={h.id}>
                  <td>{h.name}</td>
                  <td>{h.location}</td>
                  <td>{h.availableSpaces} / {h.totalCapacity}</td>
                  <td><span className={'status ' + (isOpen ? 'status-AVAILABLE' : 'status-FULL')}>{isOpen ? 'Open' : 'Full'}</span></td>
                  <td>{h.warden ? h.warden.fullName : '-'}</td>
                  <td className="actions">
                    <button className="btn btn-secondary btn-sm" onClick={() => openEdit(h)}>Edit</button>
                    <button className="btn btn-danger btn-sm" onClick={() => handleDelete(h.id)}>Delete</button>
                  </td>
                </tr>
              )
            })}
          </tbody>
        </table>
      )}

      {showForm && (
        <div style={{ marginTop: 20 }}>
          <h3>{editingId ? 'Edit Hostel' : 'Add Hostel'}</h3>
          {error && <div className="alert">{error}</div>}
          <form className="form-box" onSubmit={handleSubmit}>
            <div className="field">
              <label>Hostel Name</label>
              <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
            </div>
            <div className="field">
              <label>Location</label>
              <input value={form.location} onChange={(e) => setForm({ ...form, location: e.target.value })} />
            </div>
            <div className="field">
              <label>Warden in Charge</label>
              <select value={form.wardenId} onChange={(e) => setForm({ ...form, wardenId: e.target.value })}>
                <option value="">-- None --</option>
                {wardens.map((w) => <option key={w.id} value={w.id}>{w.fullName}</option>)}
              </select>
            </div>
            <button type="submit" className="btn btn-primary">Save</button>
            <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)} style={{ marginLeft: 8 }}>Cancel</button>
          </form>
        </div>
      )}
    </div>
  )
}
