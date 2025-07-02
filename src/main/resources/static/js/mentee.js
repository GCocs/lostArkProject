document.addEventListener('DOMContentLoaded', function() {
    const myRequestDropdown = document.getElementById("messageDropdownMenu");
    if (!myRequestDropdown) return;

    function clearMyRequests() {
        while (myRequestDropdown.firstChild) {
            myRequestDropdown.removeChild(myRequestDropdown.firstChild);
        }
        // 고정 메뉴 추가 (멘티는 "내가 보낸 멘토링 신청 결과"만)
        const link = document.createElement('a');
        link.href = '/message/myRequest';
        link.className = 'dropdown-item text-center';
        link.textContent = '내가 보낸 멘토링 신청 결과';
        myRequestDropdown.appendChild(link);
    }

    function updateMyRequests(data) {
        clearMyRequests();
        if (!data || data.length === 0) return;
        data.forEach(request => {
            const newMessage = document.createElement("a");
            newMessage.href = "#";
            newMessage.className = "dropdown-item";
            let statusMessage = '';
            let statusColor = '';
            switch (request.apply_status) {
                case 'ACCEPTED':
                    statusMessage = '멘토링이 수락되었습니다.';
                    statusColor = 'text-success'; // 초록색
                    break;
                case 'REJECTED':
                    statusMessage = '멘토링이 거절당했습니다.';
                    statusColor = 'text-danger'; // 빨간색
                    break;
                default:
                    statusMessage = '멘토링 신청이 접수되었습니다.';
                    statusColor = '';
            }
            newMessage.innerHTML = `
                <div class="d-flex align-items-center">
                    <div class="flex-grow-1">
                        <span class="fw-bold">${request.mentor_nickname || '알 수 없음'}</span>
                        <div class="small text-gray-500 ${statusColor}">
                            ${statusMessage}
                        </div>
                    </div>
                </div>
            `;
            myRequestDropdown.insertBefore(newMessage, myRequestDropdown.firstChild);
        });
    }

    function checkMyRequests() {
        fetch('/message/my-applies')
            .then(response => response.json())
            .then(data => updateMyRequests(data));
    }

    checkMyRequests();
    setInterval(checkMyRequests, 30000);
}); 