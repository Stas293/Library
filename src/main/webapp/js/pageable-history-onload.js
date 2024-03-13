const urlPath = `/library/history/user`;

window.onload = () => {
    setHistoryListeners(urlPath);
    wizard(urlPath);
}

class HistoryOrder {
    constructor(id, bookTitle, dateCreated, dateReturned, status) {
        this.id = id;
        this.bookTitle = bookTitle;
        this.dateCreated = dateCreated;
        this.dateReturned = dateReturned;
        this.status = status;
    }

    static from(json) {
        return new HistoryOrder(
            json.id,
            json.bookTitle,
            new Date(json.dateCreated).toDateString(),
            json.dateReturned ? new Date(json.dateReturned).toDateString() : 'Not accepted',
            json.status
        );
    }
}

const makeRow = (rowData) => {
    rowData = HistoryOrder.from(rowData);
    console.log(rowData);

    let tableRow = document.createElement('tr');

    let tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.bookTitle))
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.dateCreated))
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.dateReturned))
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');

    tableData.appendChild(
        document
            .createTextNode(rowData.status.value))
    tableRow.appendChild(tableData);
    return tableRow;
}