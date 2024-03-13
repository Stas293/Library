const urlPath = `/library/books/page`;
const moreInfoUrl = `/library/books/admin`;

window.onload = () => {
    setBookListeners(urlPath);
    wizard(urlPath);
}

const makeRow = (rowData, index) => {
    console.log(rowData);
    console.log(index);
    let tableRow = document.createElement('tr');
    let tableData = document.createElement('td');
    let anchor = document.createElement('a');

    let modal = document.createElement('div');
    modal.className = 'modal fade';
    modal.id = `book${index}`;
    let modalDialog = document.createElement('div');
    modalDialog.className = 'modal-dialog modal-dialog-centered';
    let modalContent = document.createElement('div');
    modalContent.className = 'modal-content';
    let modalHeader = document.createElement('div');
    modalHeader.className = 'modal-header';
    let modalTitle = document.createElement('h5');
    modalTitle.className = 'modal-title';
    modalTitle.innerHTML = rowData.title;
    let closeBtn = document.createElement('button');
    closeBtn.className = 'btn-close';
    closeBtn.setAttribute('data-bs-dismiss', 'modal');
    let modalBody = document.createElement('div');
    modalBody.className = 'modal-body';
    let modalFooter = document.createElement('div');
    modalFooter.className = 'modal-footer';

    let moreInfoBtn = document.createElement('a');
    moreInfoBtn.className = 'btn btn-primary';
    moreInfoBtn.innerHTML = document.getElementById("more_info").innerHTML;
    moreInfoBtn.href = moreInfoUrl + `/${rowData.id}`;

    modalFooter.appendChild(moreInfoBtn);
    // add description, isbn, publication date, and author to modal body
    let description = document.createElement('p');
    description.innerHTML = rowData.description;
    modalBody.appendChild(description);
    let isbn = document.createElement('p');
    isbn.innerHTML = document.getElementById("book_ISBN").innerHTML + `${rowData.isbn}`;
    modalBody.appendChild(isbn);
    let publicationDate = document.createElement('p');
    publicationDate.innerHTML = document.getElementById("book_date_published").innerHTML + `${rowData.publicationDate}`;
    modalBody.appendChild(publicationDate);
    let author = document.createElement('p');
    author.innerHTML = document.getElementById("book_authors_label").innerHTML + `${rowData.authors}`;
    modalBody.appendChild(author);
    let count = document.createElement('p');
    count.innerHTML = document.getElementById("book_count").innerHTML + `${rowData.count}`;
    modalBody.appendChild(count);
    let fine = document.createElement('p');
    fine.innerHTML = document.getElementById("book_fine").innerHTML + `${rowData.fine}`;
    modalBody.appendChild(fine);
    let language = document.createElement('p');
    language.innerHTML = document.getElementById("book_language").innerHTML + `${rowData.language}`;
    modalBody.appendChild(language);
    let location = document.createElement('p');
    location.innerHTML = document.getElementById("book_location").innerHTML + `${rowData.location}`;
    modalBody.appendChild(location);
    modalHeader.appendChild(modalTitle);
    modalHeader.appendChild(closeBtn);
    modalContent.appendChild(modalHeader);
    modalContent.appendChild(modalBody);
    modalContent.appendChild(modalFooter);
    modalDialog.appendChild(modalContent);
    modal.appendChild(modalDialog);
    anchor.setAttribute('data-bs-toggle', 'modal');
    anchor.setAttribute('data-bs-target', `#book${index}`);
    anchor.innerHTML = rowData.title;
    tableData.appendChild(anchor);
    tableRow.appendChild(tableData);
    tableRow.appendChild(modal);

    isbn = document.createElement('td');
    isbn.innerHTML = rowData.isbn;
    tableRow.appendChild(isbn);

    publicationDate = document.createElement('td');
    publicationDate.innerHTML = rowData.publicationDate;
    tableRow.appendChild(publicationDate);

    author = document.createElement('td');
    author.innerHTML = rowData.authors;
    tableRow.appendChild(author);

    return tableRow;
}