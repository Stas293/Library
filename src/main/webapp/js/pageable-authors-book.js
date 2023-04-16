const urlPath = `/library/admin/authors-page`;

window.onload = () => {
    wizard(urlPath, null);
}

const addAuthors = () => {
    if (document.getElementById("authors-list").children.length === 0) {
        return;
    }
    let authorsList = document.getElementById("authors-list");
    let authors = [];
    for (const element of authorsList.children) {
        authors.push(element.id);
    }
    let hiddenAuthors = document.createElement("input");
    hiddenAuthors.setAttribute("type", "hidden");
    hiddenAuthors.setAttribute("name", "authors");
    hiddenAuthors.setAttribute("value", authors.join(","));
    let form = document.getElementById("add-book-form");
    form.appendChild(hiddenAuthors);
}

const makeRow = (rowData, index) => {
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');
    let label = document.createElement('label');

    anchor.setAttribute('href', '#');
    let authorsList = document.getElementById("authors-list");
    for (const element of authorsList.children) {
        element.onmouseover = () => {
            element.style.color = "red";
        };
        element.onmouseout = () => {
            element.style.color = "black";
        };
        element.onclick = () => {
            authorsList.removeChild(element);
        };
    }

    anchor.onclick = () => {
        let authorsList = document.getElementById("authors-list");
        if (authorsList.contains(document.getElementById(rowData.id))) {
            return;
        }
        let author = document.createElement("li");
        author.setAttribute("id", rowData.id);
        author.appendChild(document.createTextNode(rowData.firstName + " " + rowData.lastName));
        author.onmouseover = () => {
            author.style.color = "red";
        }
        author.onmouseout = () => {
            author.style.color = "black";
        }
        author.onclick = () => {
            let author = document.getElementById(rowData.id);
            author.remove();
        }
        authorsList.appendChild(author);
    }

    label.setAttribute('id', 'view-order-modal-window');

    label.appendChild(document.createTextNode(rowData.id));
    anchor.appendChild(label);
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.firstName));
    tableRow.appendChild(tableData);

    tableData = document.createElement('td');
    tableData.appendChild(
        document
            .createTextNode(rowData.lastName));
    tableRow.appendChild(tableData);
    return tableRow;
}