import { useEffect, useState } from 'react'
import apiClient from '../api/client'

const emptyForm = { fullName: '', email: '', phone: '', registrationNumber: '', programme: '', gender: 'Male' }

export default function Students() {
  const [students, setStudents] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState(emptyForm)
  const [editingId, setEditingId] = useState(null)
  const [error, setError] = useState('')

  async function load() {
    const res = await apiClient.get('/students')
    setStudents(res.data)
  }

  useEffect(() => { load() }, [])

  function openNew() {
    setForm(emptyForm)
    setEditingId(null)
    setError('')
    setShowForm(true)
  }

  function openEdit(student) {
    setForm({
      fullName: student.fullName, email: student.email, phone: student.phone,
      registrationNumber: student.registrationNumber, programme: student.programme || '',
      gender: student.gender || 'Male',
    })
    setEditingId(student.id)
    setError('')
    setShowForm(true)
  }

  async function handleSubmit(e) {
    e.preventDefault()
    try {
      if (editingId) {
        await apiClient.put(`/students/${editingId}`, form)
      } else {
        await apiClient.post('/students', form)
      }
      setShowForm(false)
      load()
    } catch (err) {
      setError(err.response?.data?.message || 'Could not save student')
    }
  }

  async function handleDelete(id) {
    if (!window.confirm('Delete this student?')) return
    await apiClient.delete(`/students/${id}`)
    load()
  }

  return (
    <div>
      <div className="page-header">
        <h1>Students</h1>
        <button className="btn btn-primary" onClick={openNew}>+ Add Student</button>
      </div>

      {students.length === 0 ? (
        <div className="empty-state">No students yet. Click "Add Student" to create one.</div>
      ) : (
        <table>
          <thead>
            <tr><th>Reg. Number</th><th>Full Name</th><th>Programme</th><th>Email</th><th>Phone</th><th>Actions</th></tr>
          </thead>
          <tbody>
            {students.map((s) => (
              <tr key={s.id}>
                <td>{s.registrationNumber}</td>
                <td>{s.fullName}</td>
                <td>{s.programme}</td>
                <td>{s.email}</td>
                <td>{s.phone}</td>
                <td className="actions">
                  <button className="btn btn-secondary btn-sm" onClick={() => openEdit(s)}>Edit</button>
                  <button className="btn btn-danger btn-sm" onClick={() => handleDelete(s.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}

      {showForm && (
        <div style={{ marginTop: 20 }}>
          <h3>{editingId ? 'Edit Student' : 'Add Student'}</h3>
          {error && <div className="alert">{error}</div>}
          <form className="form-box" onSubmit={handleSubmit}>
            <div className="field">
              <label>Full Name</label>
              <input value={form.fullName} onChange={(e) => setForm({ ...form, fullName: e.target.value })} required />
            </div>
            <div className="field">
              <label>Registration Number</label>
              <input value={form.registrationNumber} onChange={(e) => setForm({ ...form, registrationNumber: e.target.value })} required />
            </div>
            <div className="field">
              <label>Programme</label>
              <input value={form.programme} onChange={(e) => setForm({ ...form, programme: e.target.value })} placeholder="e.g. BSc. Computer Science" />
            </div>
            <div className="field">
              <label>Gender</label>
              <select value={form.gender} onChange={(e) => setForm({ ...form, gender: e.target.value })}>
                <option value="Male">Male</option>
                <option value="Female">Female</option>
              </select>
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
