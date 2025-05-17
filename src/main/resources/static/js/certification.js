import { valid } from './validation.js';

let imageApiTimer = null;

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
    // 1. 과도한 api 요청 방지를 위한 스로틀링
    if(imageApiTimer) {
        alert('잠시 후 다시 시도해주세요.');
        return;
    }
    
    imageApiTimer = setTimeout(() => {
        console.log('인증요청 가능');
        imageApiTimer = null;
    }, 5000);
    
    // 2. 캐릭터 닉네임 검증
    const $nickname = $('#nickname');
    if(!valid('nickname', $nickname.val())) {
        renderCharacterImage(null);
        $nickname.focus();
        return;
    }
    
    // 3. api 요청
    $.ajax({
        url: `/member/${$nickname.val()}/profiles`,
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
    $('.image-container').empty();

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