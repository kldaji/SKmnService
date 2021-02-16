//
//  ViewController.swift
//  TransDemo
//
//  Created by 장진우 on 2021/02/16.
//

import UIKit

class DoneViewController: UIViewController {
    
    @IBOutlet weak var resultLabel: UILabel!
    
    let result: String
    
    init?(coder: NSCoder, result: String) {
        self.result = result
        super.init(coder: coder)
    }
    
    convenience required init?(coder: NSCoder) {
        self.init(coder: coder, result: "Some error ocurred")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
        resultLabel.text = self.result
    }
    
}

