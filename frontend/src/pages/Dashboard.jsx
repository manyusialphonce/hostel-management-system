import { useEffect, useState } from 'react'
import apiClient from '../api/client'

export default function Dashboard() {
  const [counts, setCounts] = useState({ students: 0, wardens: 0, hostels: 0, rooms: 0, bookings: 0 })
  const [hostels, setHostels] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    async function loadData() {
      const [students, wardens, hostelsRes, rooms, bookings] = await Promise.all([
        apiClient.get('/students'),
        apiClient.get('/wardens'),
        apiClient.get('/hostels'),
        apiClient.get('/rooms'),
        apiClient.get('/bookings'),
      ])

      setCounts({
        students: students.data.length,
        wardens: wardens.data.length,
        hostels: hostelsRes.data.length,
        rooms: rooms.data.length,
        bookings: bookings.data.length,
      })
      setHostels(hostelsRes.data)
      setLoading(false)
    }
    loadData()
  }, [])

  if (loading) return <p>Loading dashboard...</p>

  return (
    <div>
      <h1>Dashboard</h1>
      <p>Overview of students, wardens, hostels, rooms and bookings.</p>

      <div className="card-grid">
        <div className="card"><div className="count">{counts.students}</div><div className="label">Students</div></div>
        <div className="card"><div className="count">{counts.wardens}</div><div className="label">Wardens</div></div>
        <div className="card"><div className="count">{counts.hostels}</div><div className="label">Hostels</div></div>
        <div className="card"><div className="count">{counts.rooms}</div><div className="label">Rooms</div></div>
        <div className="card"><div className="count">{counts.bookings}</div><div className="label">Bookings</div></div>
      </div>

      <div className="section-title">Hostels &amp; Availability</div>

      {hostels.length === 0 ? (
        <div className="empty-state">No hostels yet.</div>
      ) : (
        <div className="hostel-panel">
          {hostels.map((hostel) => {
            const pctFull = hostel.totalCapacity > 0
              ? Math.round((hostel.totalOccupants * 100) / hostel.totalCapacity)
              : 0
            const isOpen = hostel.availableSpaces > 0
            return (
              <div className="hostel-card" key={hostel.id}>
                <div className="hostel-card-header">
                  <div>
                    <h3>{hostel.name}</h3>
                    <div className="location">{hostel.location}</div>
                  </div>
                  <span className={'status ' + (isOpen ? 'status-AVAILABLE' : 'status-FULL')}>
                    {isOpen ? 'Open' : 'Full'}
                  </span>
                </div>

                {hostel.totalCapacity > 0 && (
                  <div className="availability-bar">
                    <div className="availability-bar-fill" style={{ width: `${pctFull}%` }}></div>
                  </div>
                )}

                <div className="hostel-stats">
                  <span>{hostel.availableSpaces} beds free</span>
                  <span>{hostel.totalOccupants} / {hostel.totalCapacity} occupied</span>
                </div>
                <div className="hostel-stats" style={{ marginTop: 6 }}>
                  <span>{hostel.availableRoomsCount} available rooms</span>
                </div>

                <div className="warden">
                  Warden: {hostel.warden ? hostel.warden.fullName : 'Not assigned'}
                </div>
              </div>
            )
          })}
        </div>
      )}
    </div>
  )
}
