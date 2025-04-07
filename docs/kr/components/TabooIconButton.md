## TabooIconButton
`TabooIconButton`은 사용자가 클릭할 수 있는 기본적인 버튼입니다. 아이콘과 텍스트를 함께 사용할 수 있습니다.

**Note**: `TabooButton`도 아이콘과 함께 사용할 수 있지만, 배경과 stroke가 없는 버튼을 만들 수 없다는 한계로 인해 `TabooIconButton`을 제공합니다.

![TabooButton](https://github.com/HanJunKwon/Taboo/blob/feature/readme/docs/assets/buttons/taboo_icon_button.png)


## 사용하기
`TabooButton`을 사용하기 위해서 다음과 같이 레이아웃 파일에 추가합니다.

### 기본 사용 예제
```xml
<com.kwon.taboo.button.TabooIconButton
    android:id="@+id/btn_solid"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:text="이전"/>
```

### 속성 사용 예제
아래 예제는 아이콘과 아이콘의 위치를 변경하는 예제입니다.

![TabooButton](https://github.com/HanJunKwon/Taboo/blob/feature/readme/docs/assets/buttons/taboo_icon_button_right_icon.png)

```xml
<com.kwon.taboo.button.TabooIconButton
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="이전"
    app:icon="@drawable/ic_round_arrow_forward_191f28_24dp"
    app:iconPosition="right"/>
```

## 구성 및 주요 속성

### Text 속성들
| 요소              | 속성                       | 관련 메서드                                             | 기본값    |
|-----------------|--------------------------|----------------------------------------------------|--------|
| **Text**        | `android:text`           | `setText(String)`<br/>`getText`                    | ` `    |
| **Color**       | `android:textColor`      | `setTextColor(ColorStateList?)`<br/>`getTextColor` | `null` |
| **Size**        | `android:textSize`       | `setTextSize(float)`<br/>`getTextSize`             | `16sp` |
| **Font Family** | `android:fontFamily`     | `setTypeface(Typeface)`<br/>`getTypeface`          | `null` |
| **Typography**  | `android:textAppearance` | `setTextAppearance`                                | `0`    | 

### Button 속성들
| 요소             | 속성                   | 관련 메서드                              | 기본값                                     |
|----------------|----------------------|-------------------------------------|-----------------------------------------|
| **Background** | `android:background` | `setBackground`<br/>`getBackground` | `R.drawable.selector_taboo_icon_button` |

### Icon 속성들
| 요소           | 속성                 | 관련 메서드                                  | 기본값    |
|--------------|--------------------|-----------------------------------------|--------|
| **Icon**     | `app:icon`         | `setIconDrawable`<br/>`getIconDrawable` | `0`    |
| **Position** | `app:iconPosition` | `setIconPosition`<br/>`getIconPosition` | `left` |