import { useEffect, useState } from 'react'
import apiClient from '../api/client'

const emptyForm = { studentId: '', roomId: '', checkInDate: '', checkOutDate: '', feeAmount: '', feePaid: false }

export default function Bookings() {
  const [bookings, setBookings] = useState([])
  const [students, setStudents] = useState([])
  const [rooms, setRooms] = useState([])
  const [wardens, setWardens] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState(emptyForm)
  const [error, setError] = useState('')
  const [approveWardenChoice, setApproveWardenChoice] = useState({})

  async function load() {
    const [bookingsRes, studentsRes, roomsRes, wardensRes] = await Promise.all([
      apiClient.get('/bookings'),
      apiClient.get('/students'),
      apiClient.get('/rooms'),
      apiClient.get('/wardens'),
    ])
    setBookings(bookingsRes.data)
    setStudents(studentsRes.data)
    setRooms(roomsRes.data)
    setWardens(wardensRes.data)
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
      const payload = {
        ...form,
        studentId: Number(form.studentId),
        roomId: Number(form.roomId),
        feeAmount: form.feeAmount ? Number(form.feeAmount) : 0,
      }
      await apiClient.post('/bookings', payload)
      setShowForm(false)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not save booking')
    }
  }

  async function handleApprove(id) {
    const wardenId = approveWardenChoice[id]
    if (!wardenId) {
      alert('Please choose a warden first')
      return
    }
    await apiClient.post(`/bookings/${id}/approve?wardenId=${wardenId}`)
    load()
  }

  async function handleReject(id) {
    const wardenId = approveWardenChoice[id]
    if (!wardenId) {
      alert('Please choose a warden first')
      return
    }
    if (!window.confirm('Reject this booking?')) return
    await apiClient.post(`/bookings/${id}/reject?wardenId=${wardenId}`)
    load()
  }

  async function handleCancel(id) {
    if (!window.confirm('Cancel this booking?')) return
    await apiClient.post(`/bookings/${id}/cancel`)
    load()
  }

  async function handleDelete(id) {
    if (!window.confirm('Delete this booking?')) return
    await apiClient.delete(`/bookings/${id}`)
    load()
  }

  return (
    <div>
      <div className="page-header">
        <h1>Bookings</h1>
        <button className="btn btn-primary" onClick={openNew}>+ New Booking</button>
      </div>

      {bookings.length === 0 ? (
        <div className="empty-state">No bookings yet. Click "New Booking" to create one.</div>
      ) : (
        <table>
          <thead>
            <tr><th>Student</th><th>Room</th><th>Check-in</th><th>Status</th><th>Fee</th><th>Approved By</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {bookings.map((b) => (
              <tr key={b.id}>
                <td>{b.student?.fullName}</td>
                <td>{b.room?.hostel?.name} - {b.room?.roomNumber}</td>
                <td>{b.checkInDate}</td>
                <td><span className={'status status-' + b.status}>{b.status}</span></td>
                <td><span className={'status ' + (b.feePaid ? 'status-PAID' : 'status-UNPAID')}>{b.feePaid ? 'Paid' : 'Unpaid'}</span></td>
                <td>{b.approvedBy?.fullName || '-'}</td>
                <td className="actions">
                  {b.status === 'PENDING' && (
                    <>
                      <select
                        value={approveWardenChoice[b.id] || ''}
                        onChange={(e) => setApproveWardenChoice({ ...approveWardenChoice, [b.id]: e.target.value })}
                      >
                        <option value="">Warden...</option>
                        {wardens.map((w) => <option key={w.id} value={w.id}>{w.fullName}</option>)}
                      </select>
                      <button className="btn btn-success btn-sm" onClick={() => handleApprove(b.id)}>Approve</button>
                      <button className="btn btn-danger btn-sm" onClick={() => handleReject(b.id)}>Reject</button>
                    </>
                  )}
                  {b.status === 'APPROVED' && (
                    <button className="btn btn-secondary btn-sm" onClick={() => handleCancel(b.id)}>Cancel</button>
                  )}
                  <button className="btn btn-danger btn-sm" onClick={() => handleDelete(b.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {showForm && (
        <div style={{ marginTop: 20 }}>
          <h3>New Booking</h3>
          {error && <div className="alert">{error}</div>}
          <form className="form-box" onSubmit={handleSubmit}>
            <div className="field">
              <label>Student</label>
              <select value={form.studentId} onChange={(e) => setForm({ ...form, studentId: e.target.value })} required>
                <option value="">-- Select Student --</option>
                {students.map((s) => <option key={s.id} value={s.id}>{s.fullName} ({s.registrationNumber})</option>)}
              </select>
            </div>
            <div className="field">
              <label>Room</label>
              <select value={form.roomId} onChange={(e) => setForm({ ...form, roomId: e.target.value })} required>
                <option value="">-- Select Room --</option>
                {rooms.map((r) => (
                  <option key={r.id} value={r.id}>
                    {r.hostel?.name} - {r.roomNumber} ({r.status})
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label>Check-in Date</label>
              <input type="date" value={form.checkInDate} onChange={(e) => setForm({ ...form, checkInDate: e.target.value })} required />
            </div>
            <div className="field">
              <label>Check-out Date (optional)</label>
              <input type="date" value={form.checkOutDate} onChange={(e) => setForm({ ...form, checkOutDate: e.target.value })} />
            </div>
            <div className="field">
              <label>Hostel Fee (TZS)</label>
              <input type="number" step="0.01" min="0" value={form.feeAmount} onChange={(e) => setForm({ ...form, feeAmount: e.target.value })} />
            </div>
            <div className="field field-inline">
              <input type="checkbox" id="feePaid" checked={form.feePaid} onChange={(e) => setForm({ ...form, feePaid: e.target.checked })} />
              <label htmlFor="feePaid" style={{ margin: 0 }}>Fee already paid</label>
            </div>
            <button type="submit" className="btn btn-primary">Save</button>
            <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)} style={{ marginLeft: 8 }}>Cancel</button>
          </form>
        </div>
      )}
    </div>
  )
}
