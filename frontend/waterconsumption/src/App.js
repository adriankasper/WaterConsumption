import './App.css';
import './index.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Dashboard from './Dashboard';
import NewMeasurement from './NewMeasurement';

function App() {
  return (
    <div className="App">
      <Navbar/>
      <Router>
            <Routes>
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/new-measurement" element={<NewMeasurement />} />
                {/* Add other routes here */}
            </Routes>
      </Router>
    </div>
  );
}

export default App;
