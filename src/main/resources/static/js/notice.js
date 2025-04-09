
$(() => {
    $('#notice-list').click((e) => {
        e.preventDefault();

        $.ajax({
            url: '/api/notices?page=1&size=5',
            type: 'GET',
            success: (data) => {
                let noticeList = '<table class="table">';
                noticeList += `
                    <thead>
                        <tr>
                            <th class="col-md-6">제목</th>
                            <th class="col-md-2">작성일</th>
                        </tr>
                    </thead>
                    <tbody>
                `;

                data.data.forEach((notice) => {
                    noticeList += `
                        <tr>
                            <td class="title pt-3 pb-3"><a href="#">${notice.title}</a></td>
                            <td class="pt-3 pb-3">${formatDateToString(notice.createdAt)}</td>
                        </tr>
                    `;
                });
                noticeList += '</tbody></table>';

                $('#noticeModal .modal-body').html(noticeList);
            },
            error: (xhr) => {
                console.error("error: ", xhr);
            }
        });
    });
});

function formatDateToString(string) {
    const date = new Date(string);
    const year = date.getFullYear().toString().substring(2, 4);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}.${month}.${day}`;
}