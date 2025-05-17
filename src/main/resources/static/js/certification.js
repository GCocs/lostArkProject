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
        url: '/member/밤피린/certification',
        method: 'GET',
        success: function(data) {
            renderCharacterImage(data);
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
    console.log($nickname);
    console.log($nickname.val());
    
    // 3. api 요청
    $.ajax({
        url: `/member/${$nickname.val()}/certification`,
        method: 'GET',
        success: function(data) {
            renderCharacterImage(data);
        },
        error: (xhr) => {
            console.error("error: ", xhr);
        }
    })
}

function renderCharacterImage(data) {
    console.log(data);
    
    $('.image-container').empty();
    
    if(data) {
        const characterImage = data.characterImage.CharacterImage;
        const equipment = data.equipment;

        const imgDom = `
            <div class="position-relative image-wrapper">
                <img class="position-absolute" src="${equipment['무기'].Icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(1 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['무기'].Grade)}" />
                <img class="position-absolute" src="${equipment['투구'].Icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(2 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['투구'].Grade)}" />
                <img class="position-absolute" src="${equipment['상의'].Icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(3 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['상의'].Grade)}" />
                <img class="position-absolute" src="${equipment['하의'].Icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(4 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['하의'].Grade)}" />
                <img class="position-absolute" src="${equipment['장갑'].Icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(5 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['장갑'].Grade)}" />
                <img class="position-absolute" src="${equipment['어깨'].Icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(6 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['어깨'].Grade)}" />
                <img class="position-absolute" src="${equipment['나침반'].Icon}" style="width:36px; left:calc(50% - 30%); transform:translate(-50%, -50%); top:calc(7 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['나침반'].Grade)}" />
                
                <img class="w-100" src="${characterImage}" />
                
                <img class="position-absolute" src="${equipment['목걸이'].Icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(1 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['목걸이'].Grade)}" />
                <img class="position-absolute" src="${equipment['귀걸이'].Icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(2 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['귀걸이'].Grade)}" />
                <img class="position-absolute" src="${equipment['귀걸이2'].Icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(3 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['귀걸이2'].Grade)}" />
                <img class="position-absolute" src="${equipment['반지'].Icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(4 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['반지'].Grade)}" />
                <img class="position-absolute" src="${equipment['반지2'].Icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(5 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['반지2'].Grade)}" />
                <img class="position-absolute" src="${equipment['팔찌'].Icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(6 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['팔찌'].Grade)}" />
                <img class="position-absolute" src="${equipment['어빌리티 스톤'].Icon}" style="width:36px; left:calc(50% + 30%); transform:translate(-50%, -50%); top:calc(7 / 8 * 100%); border-radius:3px; background-color:${selectBackground(equipment['어빌리티 스톤'].Grade)}" />
            </div>
        `;
        $('.image-container').html(imgDom);
    } else {
        const imgDom = `
            <img class="w-100" src="/img/sad_loacon${Math.ceil(Math.random() * 4)}.png" />
            <p>해당 캐릭터의 이미지를 불러오지 못했습니다..</p>
        `;
        $('.image-container').html(imgDom);
    }
}

function selectBackground(type) {
    if(type === '일반') return '#595959';
    if(type === '고급') return '#3C5B13';
    if(type === '희귀') return '#113E5E';
    if(type === '영웅') return '#4F1464';
    if(type === '전설') return '#B06900';
    if(type === '유물') return '#AA4101';
    if(type === '고대') return '#DFC786';
    if(type === '에스더') return '#3EF5EB';

    return '';
}