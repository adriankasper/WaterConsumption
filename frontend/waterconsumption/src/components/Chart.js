import React, { useState, useEffect } from 'react';
import ApexCharts from 'react-apexcharts';

const Chart = ({ apartmentId }) => {
    const [series, setSeries] = useState([]);
    const [options, setOptions] = useState({
        chart: {
            type: 'bar',
            height: 350,
            toolbar: {
                show: true
            }
        },
        plotOptions: {
            bar: {
                horizontal: false,
                columnWidth: '100%', 
                endingShape: 'rounded'
            }
        },
        xaxis: {
            categories: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
        },
        tooltip: {
            theme: 'dark'
        },
        stroke: {
            curve: 'smooth',
        },
        colors: ['#FF6B6B', '#4A90E2', '#FF3E3E', '#5DADE2']
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch(`${process.env.REACT_APP_BACKEND_URL}/apartmentMeasurements?aptId=${apartmentId}&startDate=${new Date().getFullYear()}-01-01&endDate=${new Date().getFullYear()}-12-01`);
                const data = await response.json();
    
                // Transform data for the chart
                const formatData = (measurements) => {
                    const months = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
                    const result = {
                        'Bathroom hot water': [],
                        'Bathroom cold water': [],
                        'Kitchen hot water': [],
                        'Kitchen cold water': []
                    };
    
                    months.forEach((month, index) => {
                        const date = `${new Date().getFullYear()}-${String(index + 1).padStart(2, '0')}-01`;
                        result['Bathroom hot water'].push(measurements.bathroomHotWater[date] || 0);
                        result['Bathroom cold water'].push(measurements.bathroomColdWater[date] || 0);
                        result['Kitchen hot water'].push(measurements.kitchenHotWater[date] || 0);
                        result['Kitchen cold water'].push(measurements.kitchenColdWater[date] || 0);
                    });
    
                    return result;
                };
    
                const formattedData = formatData(data);
                setSeries([
                    { name: 'Bathroom hot water', data: formattedData['Bathroom hot water'] },
                    { name: 'Bathroom cold water', data: formattedData['Bathroom cold water'] },
                    { name: 'Kitchen hot water', data: formattedData['Kitchen hot water'] },
                    { name: 'Kitchen cold water', data: formattedData['Kitchen cold water'] }
                ]);
    
            } catch (error) {
                console.error('Error fetching chart data:', error);
            }
        };
    
        fetchData();
    }, [apartmentId]);

    return (
        <div>
            <div className="dropdown">
                <div tabIndex={0} role="button" className="btn m-1">Timeframe</div>
                <ul tabIndex={0} className="dropdown-content menu bg-base-100 rounded-box z-[1] w-52 p-2 shadow">
                    <li><a>This year</a></li>
                    <li><a>All time</a></li>
                </ul>
            </div>
            <h1>Water usage ({new Date().getFullYear()})</h1>
            <ApexCharts
                options={options}
                series={series}
                type="bar"
                height={350}
            />
        </div>
    );
};

export default Chart;