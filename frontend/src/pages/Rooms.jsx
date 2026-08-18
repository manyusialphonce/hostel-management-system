import { useEffect, useState } from 'react'
import apiClient from '../api/client'

const emptyForm = { roomNumber: '', roomType: 'SINGLE', capacity: 1, status: 'AVAILABLE', hostelId: '' }

export default function Rooms() {
  const [rooms, setRooms] = useState([])
  const [hostels, setHostels] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState(emptyForm)
  const [editingId, setEditingId] = useState(null)
  const [error, setError] = useState('')

  async function load() {
    const [roomsRes, hostelsRes] = await Promise.all([
      apiClient.get('/rooms'),
      apiClient.get('/hostels'),
    ])
    setRooms(roomsRes.data)
    setHostels(hostelsRes.data)
  }

  useEffect(() => { load() }, [])

  function openNew() {
    setForm(emptyForm)
    setEditingId(null)
    setError('')
    setShowForm(true)
  }

  function openEdit(room) {
    setForm({
      roomNumber: room.roomNumber, roomType: room.roomType, capacity: room.capacity,
      status: room.status, hostelId: room.hostel?.id || '',
    })
    setEditingId(room.id)
    setError('')
    setShowForm(true)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    try {
      const payload = { ...form, capacity: Number(form.capacity), hostelId: Number(form.hostelId) }
      if (editingId) {
        await apiClient.put(`/rooms/${editingId}`, payload)
      } else {
        await apiClient.post('/rooms', payload)
      }
      setShowForm(false)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not save room')
    }
  }

  async function handleDelete(id) {
    if (!window.confirm('Delete this room?')) return
    await apiClient.delete(`/rooms/${id}`)
    load()
  }

  return (
    <div>
      <div className="page-header">
        <h1>Rooms</h1>
        <button className="btn btn-primary" onClick={openNew}>+ Add Room</button>
      </div>

      {rooms.length === 0 ? (
        <div className="empty-state">No rooms yet. Click "Add Room" to create one.</div>
      ) : (
        <table>
          <thead>
            <tr><th>Room No.</th><th>Hostel</th><th>Type</th><th>Capacity</th><th>Occupants</th><th>Status</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {rooms.map((r) => (
              <tr key={r.id}>
                <td>{r.roomNumber}</td>
                <td>{r.hostel?.name}</td>
                <td>{r.roomType}</td>
                <td>{r.capacity}</td>
                <td>{r.occupantsCount}</td>
                <td><span className={'status status-' + r.status}>{r.status}</span></td>
                <td className="actions">
                  <button className="btn btn-secondary btn-sm" onClick={() => openEdit(r)}>Edit</button>
                  <button className="btn btn-danger btn-sm" onClick={() => handleDelete(r.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {showForm && (
        <div style={{ marginTop: 20 }}>
          <h3>{editingId ? 'Edit Room' : 'Add Room'}</h3>
          {error && <div className="alert">{error}</div>}
          <form className="form-box" onSubmit={handleSubmit}>
            <div className="field">
              <label>Room Number</label>
              <input value={form.roomNumber} onChange={(e) => setForm({ ...form, roomNumber: e.target.value })} required />
            </div>
            <div className="field">
              <label>Hostel</label>
              <select value={form.hostelId} onChange={(e) => setForm({ ...form, hostelId: e.target.value })} required>
                <option value="">-- Select Hostel --</option>
                {hostels.map((h) => <option key={h.id} value={h.id}>{h.name}</option>)}
              </select>
            </div>
            <div className="field">
              <label>Room Type</label>
              <select value={form.roomType} onChange={(e) => setForm({ ...form, roomType: e.target.value })}>
                <option value="SINGLE">SINGLE</option>
                <option value="DOUBLE">DOUBLE</option>
                <option value="SHARED">SHARED</option>
              </select>
            </div>
            <div className="field">
              <label>Capacity</label>
              <input type="number" min="1" value={form.capacity} onChange={(e) => setForm({ ...form, capacity: e.target.value })} required />
            </div>
            <div className="field">
              <label>Status</label>
              <select value={form.status} onChange={(e) => setForm({ ...form, status: e.target.value })}>
                <option value="AVAILABLE">AVAILABLE</option>
                <option value="FULL">FULL</option>
                <option value="UNDER_MAINTENANCE">UNDER_MAINTENANCE</option>
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
