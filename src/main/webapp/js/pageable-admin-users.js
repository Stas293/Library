let urlPath = `/library/admin/manage`;

window.onload = () => {
    setUsersListeners(urlPath);
    wizard(urlPath, null);
}

const makeRow = (rowData) => {
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');
    anchor.setAttribute('href', `${urlPath}/${rowData.id}`);
    anchor.appendChild(document.createTextNode(rowData.login));
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(
                rowData.firstName
            ));

    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(
                rowData.lastName
            ))
    tableRow.appendChild(tableData);
    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.phone))
    tableRow.appendChild(tableData);
    return tableRow;
}