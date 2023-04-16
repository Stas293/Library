const urlPath = `/library/user/order-history`;

window.onload = () => {
    wizard(urlPath, null);
}

const makeRow = (rowData) => {
    let tableRow = document.createElement('tr');

    let tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.bookName))
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.dateCreated))
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.dateExpire))
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');

    tableData.appendChild(
        document
            .createTextNode(rowData.status.value))
    tableRow.appendChild(tableData);
    return tableRow;
}