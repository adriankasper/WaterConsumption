import React, { useState, useEffect } from 'react';
import './App.css';
import './index.css';

function NewMeasurement() {
    const [alertVisible, setAlertVisible] = useState("hidden");
    const [alertText, setAlertText] = useState('');
    const [alertType, setAlertType] = useState('alert-success');
    const [alertIcon, setAlertIcon] = useState('M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z');

    const [apartments, setApartments] = useState([]);


    const currentYear = new Date().getFullYear();
    const years = [];
    for (let year = currentYear; year >= 1970; year--) {
        years.push(year);
    }

    const months = [
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    ];

    useEffect(() => {
        // Fetch apartment data
        fetch(`${process.env.REACT_APP_BACKEND_URL}/getAllApartments`)
            .then(response => response.json())
            .then(data => setApartments(data))
            .catch(error => {
                console.error('Error fetching apartment data:', error);
            });
    }, []);

    const handlePostNewMeasurement = async (event) => {
        event.preventDefault();

        const form = event.target;
        const apartment = form.querySelector('select[name="apartment"]').value;
        const year = form.querySelector('select[name="year"]').value;
        const month = form.querySelector('select[name="month"]').value;
        const kitchenHotWater = form.querySelector('input[name="kitchenHotWater"]').value;
        const kitchenColdWater = form.querySelector('input[name="kitchenColdWater"]').value;
        const bathroomHotWater = form.querySelector('input[name="bathroomHotWater"]').value;
        const bathroomColdWater = form.querySelector('input[name="bathroomColdWater"]').value;

        const date = `${year}-${month.padStart(2, '0')}-01`;

        const payload = {
            aptId: parseInt(apartment),
            date,
            kitchenHotWater: parseFloat(kitchenHotWater),
            kitchenColdWater: parseFloat(kitchenColdWater),
            bathroomHotWater: parseFloat(bathroomHotWater),
            bathroomColdWater: parseFloat(bathroomColdWater)
        };
        console.log(`${process.env.REACT_APP_BACKEND_URL}/measurements`)
        console.log(payload)

        try {
            const response = await fetch(`${process.env.REACT_APP_BACKEND_URL}/measurements`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload),
            });

            if (response.ok) {
                setAlertText('Success!');
                setAlertType('alert-success');
                setAlertIcon('M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z');
            } else if (response.status === 409) {
                const errorMessage = await response.text();
                setAlertText(errorMessage);
                setAlertType('alert-error');
                setAlertIcon('M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z');
            } else {
                throw new Error('Network response was not ok');
            }

        } catch (error) {
            console.error('There was a problem with the fetch operation:', error);
        } finally {
            setAlertVisible(""); // Show the alert
        }
    };

    return (
        <div className="NewMeasurement container p-4 mx-auto">
            <h1>This is the best measurement form you have ever seen</h1>

            <div role="alert" className={`alert ${alertType} ${alertVisible} my-2`}>
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-6 w-6 shrink-0 stroke-current"
                    fill="none"
                    viewBox="0 0 24 24">
                    <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth="2"
                        d={`${alertIcon}`}/>
                </svg>
                <span name="alertText">{alertText}</span>
            </div>

            <div className="flex flex-col border-opacity-50 w-full">
                <div className="divider">Apartment</div>
            </div>
            <form onSubmit={handlePostNewMeasurement}>
            <select name="apartment" className="select select-bordered w-full max-w-xs my-2" required>
                    <option disabled selected>Apartment</option>
                    {apartments.map(apartment => (
                        <option key={apartment.id} value={apartment.id}>{apartment.aptNumber}</option>
                    ))}
                </select>
                <div className="flex flex-col border-opacity-50 w-full">
                    <div className="divider">Time</div>
                </div>
                <select name="year" className="select select-bordered w-full max-w-xs my-2" required>
                    <option disabled selected>Year</option>
                    {years.map(year => (
                        <option key={year} value={year}>{year}</option>
                    ))}
                </select>
                <br />
                <select name="month" className="select select-bordered w-full max-w-xs my-2" required>
                    <option disabled selected>Month</option>
                    {months.map((month, index) => (
                        <option key={index + 1} value={index + 1}>{month}</option>
                    ))}
                </select>
                <div className="flex flex-col border-opacity-50 w-full">
                    <div className="divider">Kitchen</div>
                </div>
                <br />
                <input type="number" name='kitchenHotWater' placeholder="Hot water" step={0.001} min={0} className="input input-bordered w-full max-w-xs my-2" required/>
                <br />
                <input type="number" name='kitchenColdWater' placeholder="Cold water" step={0.001} min={0} className="input input-bordered w-full max-w-xs my-2" required/>
                <br />
                <div className="flex flex-col border-opacity-50 w-full">
                    <div className="divider">Bathroom</div>
                </div>
                <input type="number" name='bathroomHotWater' placeholder="Hot water" step={0.001} min={0} className="input input-bordered w-full max-w-xs my-2" required/>
                <br />
                <input type="number" name='bathroomColdWater' placeholder="Cold water" step={0.001} min={0} className="input input-bordered w-full max-w-xs my-2" required/>
                <br />
                <input type="submit" className="btn my-5" />
            </form>
        </div>
    );
}

export default NewMeasurement;
