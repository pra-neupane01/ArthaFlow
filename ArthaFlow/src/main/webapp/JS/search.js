function toggleSearchInputs() {
    let type = document.getElementById('searchType').value;
    document.getElementById('typeInput').style.display = (type === 'type') ? 'inline' : 'none';
    document.getElementById('dateInputs').style.display = (type === 'date') ? 'inline' : 'none';
}