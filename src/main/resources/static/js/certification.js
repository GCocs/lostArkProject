$(() => {
    // 이벤트 바인딩
    $('#certification-button').click(fetchCharacterImage);
    $('#nickname').keydown((e) => {
        if(e.key === 'Enter') {
            fetchCharacterImage();
        }
    });

    // 페이지 로드 후 예시 이미지 렌더링
    $.ajax({
        url: '/member/밤피린/profiles',
        method: 'GET',
        success: function(data) {
            renderCharacterImage(data.CharacterImage);
        },
        error: (xhr) => {
            console.error("error: ", xhr);
        }
    })
});

// 캐릭터 이미지 불러오기
function fetchCharacterImage() {
    $('.image-container').empty();

    const nickname = $('#nickname').val();
    if(!nickname) {
        renderCharacterImage(null);
        return;
    }

    $.ajax({
        url: `/member/${nickname}/profiles`,
        method: 'GET',
        success: function(data) {
            renderCharacterImage(data.CharacterImage);
        },
        error: (xhr) => {
            console.error("error: ", xhr);
        }
    })
}

function renderCharacterImage(image) {
    if(image) {
        const imgDom = `<img class="w-100" src="${image}" style="max-width:500px;" />`;
        $('.image-container').html(imgDom);
    } else {
        const imgDom = `
            <img class="w-100" src="/img/sad_loacon${Math.ceil(Math.random() * 4)}.png" style="max-width:500px;" />
            <p>해당 캐릭터의 이미지를 불러오지 못했습니다..</p>
        `;
        $('.image-container').html(imgDom);
    }
}