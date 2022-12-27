const urlPath = `/library/books-page`;

window.onload = () => {
    setBookListeners(urlPath);
    wizard(urlPath);
}

const makeRow = (rowData) => {
    let tableRow = document.createElement('tr');

    let tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.name))
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.isbn))
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.publicationDate))
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.style.width = '25%';
    let authorsString = rowData.authors.map(author => `${author.firstName} ${author.lastName}`).join(', ');
    tableData.appendChild(
        document
            .createTextNode(
                authorsString))
    tableRow.appendChild(tableData);
    return tableRow;
}