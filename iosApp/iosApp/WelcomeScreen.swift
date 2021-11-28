import SwiftUI
import shared

struct WelcomeScreen: ConnectedView {
    typealias S = WelcomeStore
    typealias M = WelcomeModel
    typealias E = WelcomeEvents
    typealias F = WelcomeEffects
    
    struct Props {
        let addCarMode: Bool
        let cars: [UiCar]
        let onAddCar: () -> Void
        let onDiscardCreateCar: () -> Void
        let onCreateCar: (UiCar) -> Void
    }
    
    func map(state: WelcomeModel, dispatch: @escaping DispatchFunction<WelcomeEvents>) -> Props {
        print("new model \(state)")
        return Props(
            addCarMode: state.addCarMode,
            cars: state.cars,
            onAddCar: { dispatch(WelcomeEvents.AddCar.shared)},
            onDiscardCreateCar: { dispatch(WelcomeEvents.DismissAddCarDialog.shared)},
            onCreateCar: { car in
                dispatch(WelcomeEvents.CreateCar(car: car))
            }
        )
    }
    
    func body(props: Props) -> some View {
        VStack {
            if (props.addCarMode) {
                AddCarView(onDiscard: props.onDiscardCreateCar, onSubmit: props.onCreateCar)
            }
            else {
                if (props.cars.isEmpty) {
                    Button("Add a car") {
                        props.onAddCar()
                    }
                }
                else {
                    ScrollView {
                        LazyVStack(alignment: .leading) {
                            ForEach(props.cars, id: \.self) { car in
                                Text("\(car.titleDisplay())")
                                    .font(.title3)
                                if let nickname = car.nickname {
                                    Text(nickname)
                                        .font(.subheadline)
                                }
                            }
                        }
                    }
                    Button("Add a car") {
                        props.onAddCar()
                    }
                    
                }
            }
        }
    }
}

struct AddCarView: View {
    @State private var year: String = ""
    @State private var make: String = ""
    @State private var model: String = ""
    @State private var trim: String = ""
    @State private var nickname: String = ""
    
    let onDiscard: () -> Void
    let onSubmit: (UiCar) -> Void
    
    init(onDiscard: @escaping () -> Void, onSubmit: @escaping (UiCar) -> Void) {
        self.onDiscard = onDiscard
        self.onSubmit = onSubmit
    }
    
    var body: some View {
        VStack {
            TextField("Year", text: $year).keyboardType(.asciiCapableNumberPad)
            TextField("Make", text: $make)
            TextField("Model", text: $model)
            TextField("trim", text: $trim)
            TextField("nickname", text: $nickname)
            HStack {
                Button("Discard") {
                    onDiscard()
                }
                Button("Submit") {
                    onSubmit(UiCar(year: Int32(Int(year) ?? 2020), make: make, model: model,
                                   trim: trim, nickname: nickname))
                }
            }
        }
    }
}
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        WelcomeScreen()
    }
}
