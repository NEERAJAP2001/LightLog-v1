import React, { useEffect, useState } from 'react';
import axios from 'axios';
import {
  BarChart, Bar, XAxis, YAxis, Tooltip, Legend, ResponsiveContainer
} from 'recharts';

const LogChart = () => {
  const [data, setData] = useState([]);

  useEffect(() => {
    fetchStats();
    const interval = setInterval(fetchStats, 2 * 60 * 1000); // every 2 minutes
    return () => clearInterval(interval);
  }, []);

  const fetchStats = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/logs/stats');
      const transformed = res.data.map(item => ({
        service: item.service,
        ...item.keywordCounts
      }));
      setData(transformed);
    } catch (err) {
      console.error('Error fetching log stats:', err);
    }
  };

  return (
    <div style={{ width: '100%', height: 400 }}>
      <h3 style={{ textAlign: 'center' }}>Log Monitoring Chart</h3>
      <ResponsiveContainer>
        <BarChart data={data}>
          <XAxis dataKey="service" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="error" fill="#e74c3c" />
          <Bar dataKey="shutdown" fill="#f39c12" />
          <Bar dataKey="exception" fill="#2980b9" />
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default LogChart;