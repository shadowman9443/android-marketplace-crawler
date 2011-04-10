class App < ActiveRecord::Base
  validates_uniqueness_of :packageName
  
  has_one :rating, :dependent => :destroy
  has_many :visuals, :dependent => :destroy
  has_many :comments, :dependent => :destroy
  
  has_many :app_targets
  has_many :targets, :through => :app_targets
  
  has_many :app_permissions
  has_many :permissions, :through => :app_permissions
  
  def self.greater_than(id)
    where("id > ?", id)
  end
end
