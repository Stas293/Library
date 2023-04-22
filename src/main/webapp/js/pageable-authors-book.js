const urlPath = `/library/authors/admin`;
const keywordsPath = `/library/keywords/admin`;

function addAlreadySelectedAuthotsAndKeywords() {
    $("#author-list li").each(function () {
        const author = {
            value: $(this).text().trim(),
            id: $(this).data("id")
        };
        $("#author-list li[data-id='" + author.id + "']").remove();
        addAuthorToList(author);
    });

    $("#keyword-list li").each(function () {
        const keyword = {
            value: $(this).text().trim(),
            id: $(this).data("id")
        };
        $("#keyword-list li[data-id='" + keyword.id + "']").remove();
        addKeywordToList(keyword);
    });
}

window.onload = () => {
    addAlreadySelectedAuthotsAndKeywords();
    createAuthorSearch(urlPath);
    createKeywordSearch(keywordsPath);
}

function addAuthorToList(author) {
    if ($("#author-list li[data-id='" + author.id + "']").length > 0) {
        return;
    }
    $("#author-list").append("<li data-id='" + author.id + "'>" + author.value + " <button class='btn btn-close btn-sm' aria-label='Close'></button></li>");
    $("#author-choices").val("");
    const authors = $("#author-list li").map(function () {
        return $(this).data("id");
    }).get().join(",");
    $("#authors-input").val(authors);
}

function createAuthorSearch(authorsPath) {
    // Handle form submission for creating a new author
    $("#createAuthorForm").submit(function (event) {
        event.preventDefault();
        console.log("submit");
        const newAuthorFirstName = $("#newAuthorFirstNameInput").val().trim();
        const newAuthorMiddleName = $("#newAuthorMiddleNameInput").val().trim();
        const newAuthorLastName = $("#newAuthorLastNameInput").val().trim();
        const newAuthor = {
            firstName: newAuthorFirstName,
            middleName: newAuthorMiddleName,
            lastName: newAuthorLastName
        };
        $.ajax({
            type: "POST",
            url: authorsPath,
            data: newAuthor,
            dataType: "json",
            success: function (data) {
                console.log(data);
                // Add the newly created author to the list
                addAuthorToList({
                    value: data.middleName !== null ? data.firstName + " " + data.middleName + " " + data.lastName
                        : data.firstName + " " + data.lastName,
                    id: data.id
                });
                // Hide the modal
                $("#createAuthorModal").modal("hide");
                $("#newAuthorFirstNameInput").val("");
                $("#newAuthorMiddleNameInput").val("");
                $("#newAuthorLastNameInput").val("");
                $("#success-alert").show();
            },
            error: function (xhr, status, error) {
                console.error(xhr.status + ": " + xhr.statusText)
                const response = JSON.parse(xhr.responseText);

                console.log(response);

                // add error messages to the modal
                if (response.containsErrors) {
                    // Show error messages for each field
                    if (response.firstName) {
                        $('#first-name-error').show();
                    } else {
                        $('#first-name-error').hide();
                    }

                    if (response.middleName) {
                        $('#middle-name-error').show();
                    } else {
                        $('#middle-name-error').hide();
                    }

                    if (response.lastName) {
                        $('#last-name-error').show();
                    } else {
                        $('#last-name-error').hide();
                    }
                }
            }
        });
    });


    $(".btn-close").on("click", function () {
        $(this).closest(".alert").hide();
    });

    $("#author-choices").autocomplete({
        source: function (request, response) {
            $.get(`${authorsPath}/list`, {query: request.term.trim()}, function (data) {
                console.log(data);
                data = JSON.parse(data);
                const authors = data.map(function (author) {
                    return {
                        value: author.middleName ?
                            author.firstName + " " + author.middleName + " " + author.lastName
                            : author.firstName + " " + author.lastName,
                        id: author.id
                    };
                });

                // Add a "Create new" option if the search term doesn't match any of the existing authors
                if (request.term.trim() && $.ui.autocomplete.filter(authors, request.term.trim()).length === 0) {
                    authors.push({
                        value: "Create new author: " + request.term,
                        id: -1
                    });
                }

                response(authors);
            });
        },

        minLength: 2,
        select: function (event, ui) {
            const author = ui.item;

            if (author.id === -1) {
                // Show the create author modal
                $("#createAuthorModal").modal("show");
                return false;
            } else {
                // If the selected item has an id, it means the user selected an existing author
                addAuthorToList(author);
                return false;
            }
        }
    });

    $("#author-list").on("click", ".btn-close", function () {
        $(this).parent().remove();
        var authors = $("#author-list li").map(function () {
            return $(this).data("id");
        }).get().join(",");
        $("#authors-input").val(authors);
    });
}


function addKeywordToList(keyword) {
    if ($("#keyword-list li[data-id='" + keyword.id + "']").length > 0) {
        return;
    }
    $("#keyword-list").append("<li data-id='" + keyword.id + "'>" + keyword.value +
        " <button class='btn btn-close btn-sm' aria-label='Close'></button></li>");
    $("#keyword-choices").val("");
    const keywords = $("#keyword-list li").map(function () {
        return $(this).data("id");
    }).get().join(",");
    $("#keywords-input").val(keywords);
}

function createKeywordSearch(keywordsPath) {
    // Handle form submission for creating a new keyword
    $("#createKeywordForm").submit(function (event) {
        event.preventDefault();
        console.log("submit");
        const newKeyword = $("#newKeywordInput").val();
        $.ajax({
            type: "POST",
            url: keywordsPath,
            data: {keyword: newKeyword},
            dataType: "json",
            success: function (data) {
                console.log(data);
                // Add the newly created keyword to the list
                addKeywordToList({
                    value: data.data,
                    id: data.id
                });
                // Hide the modal
                $("#createKeywordModal").modal("hide");
                $("#newKeywordInput").val("");
                $("#success-message").text("New keyword added successfully.");
                $("#success-alert").show();
            },
            error: function (xhr, status, error) {
                console.error(xhr.status + ": " + xhr.statusText)
                $("#createKeywordModal").modal("hide");
                $("#error-message").text("An error occurred while adding the keyword.");
                $("#error-alert").show();
            }
        });
    });

    $(".btn-close").on("click", function () {
        $(this).closest(".alert").hide();
    });

    $("#keyword-choices").autocomplete({
        source: function (request, response) {
            $.get(keywordsPath, {query: request.term.trim()}, function (data) {
                console.log(data);
                data = JSON.parse(data);
                const keywords = data.map(function (keyword) {
                    return {
                        value: keyword.data,
                        id: keyword.id
                    };
                });

                // Add a "Create new" option if the search term doesn't match any of the existing keywords
                if (request.term.trim() && $.ui.autocomplete.filter(keywords, request.term.trim()).length === 0) {
                    keywords.push({
                        value: "Create new keyword: " + request.term,
                        id: -1
                    });
                }

                response(keywords);
            });
        },

        minLength: 2,
        select: function (event, ui) {
            const keyword = ui.item;

            if (keyword.id === -1) {
                // Show the create keyword modal
                $("#createKeywordModal").modal("show");
                return false;
            } else {
                // If the selected item has an id, it means the user selected an existing keyword
                addKeywordToList(keyword);
                return false;
            }
        }
    });

    $("#keyword-list").on("click", ".btn-close", function () {
        $(this).parent().remove();
        var keywords = $("#keyword-list li").map(function () {
            return $(this).data("id");
        }).get().join(",");
        $("#keywords-input").val(keywords);
    });
}
