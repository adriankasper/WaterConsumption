import React, { useState, useEffect } from 'react';
import './App.css';
import Chart from './components/Chart';
import './index.css';

function Dashboard() {
  const [apartments, setApartments] = useState([]);
  const [measurements, setMeasurements] = useState({});
  const currentDate = new Date();
  const startDate = `${currentDate.getFullYear()}-${String(currentDate.getMonth() + 1).padStart(2, '0')}-01`;
  const endDate = startDate; // Same date for the current month

  useEffect(() => {
    const fetchData = async () => {
        try {
            const apartmentResponse = await fetch(`${process.env.REACT_APP_BACKEND_URL}/getAllApartments`);
            const apartmentData = await apartmentResponse.json();
            setApartments(apartmentData);

            // Fetch measurements in parallel
            const measurementsPromises = apartmentData.map(apartment => 
                fetch(`${process.env.REACT_APP_BACKEND_URL}/apartmentMeasurements?aptId=${apartment.id}&startDate=${startDate}&endDate=${endDate}`)
                    .then(response => response.json())
                    .then(measurementData => ({
                        id: apartment.id,
                        measurements: measurementData
                    }))
            );

            const measurementsData = await Promise.all(measurementsPromises);
            const measurementsMap = measurementsData.reduce((acc, { id, measurements }) => {
                acc[id] = measurements;
                return acc;
            }, {});
            setMeasurements(measurementsMap);

        } catch (error) {
            console.error('Error fetching data:', error);
        }
    };

    fetchData();
}, [startDate, endDate]);
  return (
    <div className="Dashboard">
      <h1>This is the best Dashboard known to mankind</h1>
      <div className="overflow-x-auto md:w-4/5 sm:w-full container p-4 mx-auto">
        <div className="join join-vertical w-full">
          {apartments.map(apartment => {
            const measurement = measurements[apartment.id] || {};
            return (
              <div className="collapse collapse-arrow join-item border-base-300 bg-base-200 border" key={apartment.id}>
                <input type="radio" name="my-accordion-4" defaultChecked={apartment.id === apartments[0]?.id} />
                <div className="collapse-title text-xl font-medium">
                  <table className="table">
                    <thead>
                      <tr>
                        <th></th>
                        <th>Apartment</th>
                        <th>Kitchen hot water</th>
                        <th>Kitchen cold water</th>
                        <th>Bathroom hot water</th>
                        <th>Bathroom cold water</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr className="hover">
                        <th>{apartment.id}</th>
                        <td>{apartment.aptNumber}</td>
                        <td>{measurement.kitchenHotWater?.[startDate] || 'N/A'}</td>
                        <td>{measurement.kitchenColdWater?.[startDate] || 'N/A'}</td>
                        <td>{measurement.bathroomHotWater?.[startDate] || 'N/A'}</td>
                        <td>{measurement.bathroomColdWater?.[startDate] || 'N/A'}</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div className="collapse-content">
                  <Chart apartmentId={apartment.id} />
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}

export default Dashboard;