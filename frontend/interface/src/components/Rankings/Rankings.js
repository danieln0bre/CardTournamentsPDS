import React, { useEffect, useState } from 'react';
import { fetchGeneralRankings } from '../../services/api';
import './Rankings.css';

function Rankings() {
    const [rankings, setRankings] = useState([]);
    const [error, setError] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 50;

    useEffect(() => {
        fetchGeneralRankings().then(data => {
            if (Array.isArray(data)) {
                setRankings(data);
            } else {
                setError('Unexpected response format');
                console.error('Expected an array but got:', data);
            }
        }).catch(error => {
            setError(error.message);
            console.error('Failed to fetch rankings', error);
        });
    }, []);

    const handlePreviousPage = () => {
        setCurrentPage(prevPage => Math.max(prevPage - 1, 1));
    };

    const handleNextPage = () => {
        setCurrentPage(prevPage => prevPage + 1);
    };

    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentItems = rankings.slice(indexOfFirstItem, indexOfLastItem);

    if (error) {
        return <div className="error">Error: {error}</div>;
    }

    return (
        <div className="rankings-container">
            <h1>Ranking Geral</h1>
            <ul className="rankings-list">
                {Array.isArray(currentItems) && currentItems.map((player, index) => (
                    <li key={player.id} className="rankings-list-item">
                        #{indexOfFirstItem + index + 1} {player.username} - Pontos: {player.rankPoints}
                    </li>
                ))}
            </ul>
            <div className="pagination">
                <button className="pagination-button" onClick={handlePreviousPage} disabled={currentPage === 1}>
                    Previous
                </button>
                <span className="pagination-info">Page {currentPage}</span>
                <button className="pagination-button" onClick={handleNextPage} disabled={indexOfLastItem >= rankings.length}>
                    Next
                </button>
            </div>
        </div>
    );
}

export default Rankings;
