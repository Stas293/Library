const urlPath = `/library/admin/authors-page`;

window.onload = () => {
    wizard(urlPath);
}

const addAuthors = () => {
    let authorsList = document.getElementById("authors-list");
    let authors = [];
    for (let i = 0; i < authorsList.children.length; i++) {
        authors.push(authorsList.children[i].id);
    }
    let hiddenAuthors = document.createElement("input");
    hiddenAuthors.setAttribute("type", "hidden");
    hiddenAuthors.setAttribute("name", "authors");
    hiddenAuthors.setAttribute("value", authors);
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
    for (let i = 0; i < authorsList.children.length; i++) {
        authorsList.children[i].onmouseover = () => {
            authorsList.children[i].style.color = "red";
        }
        authorsList.children[i].onmouseout = () => {
            authorsList.children[i].style.color = "black";
        }
        authorsList.children[i].onclick = () => {
            authorsList.removeChild(authorsList.children[i]);
        }
    }
    anchor.onclick = () => {
        let authorsList = document.getElementById("authors-list");
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